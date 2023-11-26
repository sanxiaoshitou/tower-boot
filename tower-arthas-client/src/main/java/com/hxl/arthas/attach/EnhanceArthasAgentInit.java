package com.hxl.arthas.attach;

import com.hxl.arthas.ArthasProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 对{@link ArthasAgent#init() ArthasAgent.init() }方法进行增强。<br>
 * 当调用{@link ArthasAgent#init()}时候，尝试解决com.sun.tools.attach.VirtualMachine加载attach动态库名的异常。即如果出现如下异常：
 * Caused by: java.lang.UnsatisfiedLinkError: Native Library xx/动态库前缀attach.动态库后缀 already loaded in another classloader则进行增强。
 * <p>
 * 目前增强方式有两种：<br>
 * 第一种是{@link #triggerSystemGc() 手工强制gc}，通过spring.arthas.tryGc控制，默认开启；<br>
 * 第二种是 {@link #removeAttachName() 通过反射移除java.lang.ClassLoader#loadedLibraryNames里面的包含attach名称的元素}，通过spring.arthas.tryRemove，默认关闭
 * <p>
 * 不同的操作系统，attach动态库的文件名前缀和后缀分别如下：<br>
 * libattach.so（linux）、libattach.dylib(mac)、attach.dll(windows)
 * <p>
 * 解决<href =https://github.com/alibaba/arthas/issues/1838></href>
 */
@Slf4j
public class EnhanceArthasAgentInit {
    private final ArthasAgent arthasAgent;
    private final boolean slientInit;

    public EnhanceArthasAgentInit(final ArthasAgent arthasAgent, ArthasProperties arthasProperties) {
        this.arthasAgent = arthasAgent;
        this.slientInit = arthasProperties.isSlientInit();
        addResolves(arthasProperties);
    }

    private void addResolves(ArthasProperties arthasProperties) {
        Runnable emptyResolve = () -> {
        };
        this.addResolves(emptyResolve);//首次init之前不做任务处理

        if (arthasProperties.isTryGc()) {
            Runnable gcResolve = this::triggerSystemGc;
            this.addResolves(gcResolve);//如果gc开关打开，则在异常之后再次init之前强制执行gc
        }
        if (arthasProperties.isTryRemove()) {
            Runnable removeResolve = this::removeAttachName;
            this.addResolves(removeResolve);//如果remove开关打开，则在异常之后再次init之前强制执行remove
        }
    }

    public void addResolves(Runnable resolve) {
        resolves.add(resolve);
    }

    public void tryInit() {
        Iterator<Runnable> resolveIterator = resolves.iterator();
        while (resolveIterator.hasNext()) {
            try {
                resolveIterator.next().run();//init前先执行run解决加载libattach.so问题
                if (this.init()) {//执行成功立刻返回
                    log.info("Arthas agent initialize success.");
                    return;
                }
            } catch (Throwable e) {
                //处理异常
                if (isLoadedInAnotherClassLoader(e) && resolveIterator.hasNext()) {
                    continue;
                }
                if (!this.slientInit) {
                    throw e;
                }
                log.error("Arthas启动异常，异常信息：" + e);
                return;//如果出现其他异常，直接返回了
            }
        }
    }

    private boolean init() throws IllegalStateException {
        arthasAgent.init();
        return true;
    }


    /**
     * 检测异常是否为UnsatisfiedLinkError实例，并且错误消息后缀为： System.mapLibraryName("attach") + " loaded in another classloader"<br>
     * 即只处理加载如下动态库名的异常：<br>
     * libattach.so（linux）、libattach.dylib(mac)、attach.dll(windows)
     */
    private boolean isLoadedInAnotherClassLoader(Throwable e) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(e);
        if (!(rootCause instanceof UnsatisfiedLinkError)
                || (rootCause.getMessage() == null)) {
            return false;
        }
        String libname = System.mapLibraryName("attach");
        String expectedErrorMessage = libname + " loaded in another classloader";
        return rootCause.getMessage().endsWith(expectedErrorMessage);
    }

    private void triggerSystemGc() {
        log.info("attach失败，尝试手工触发gc");
        System.gc();
    }

    /**
     * 通过反射移除java.lang.ClassLoader#loadedLibraryNames里面的包含attach名称的值
     */
    private void removeAttachName() {
        String name = System.mapLibraryName("attach");
        try {
            Field loadedLibraryNames = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            loadedLibraryNames.setAccessible(true);
            Collection<String> libnames = (Collection<String>) loadedLibraryNames.get(null);
            Iterator<String> iterator = libnames.iterator();
            while (iterator.hasNext()) {
                String libname = iterator.next();
                if (libname.endsWith(name)) {
                    log.info("attach失败，移除{}，解除jvm加载lib的限定：同一个lib只能被同一个ClassLoader类加载器加载", libname);
                    iterator.remove();
                    return;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private List<Runnable> resolves = new ArrayList<>();
}
