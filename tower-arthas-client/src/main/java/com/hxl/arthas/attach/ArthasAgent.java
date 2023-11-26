package com.hxl.arthas.attach;

import com.hxl.arthas.constant.ArthasClientConstant;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.zeroturnaround.zip.ZipUtil;

import java.arthas.SpyAPI;
import java.io.File;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class ArthasAgent {
    private static final int TEMP_DIR_ATTEMPTS = 10000;

    private static final String ARTHAS_CORE_JAR = "arthas-core.jar";
    private static final String ARTHAS_BOOTSTRAP = "com.taobao.arthas.core.server.ArthasBootstrap";
    private static final String GET_INSTANCE = "getInstance";
    private static final String DESTROY = "destroy";
    private static final String IS_BIND = "isBind";

    private String errorMessage;

    private final Map<String, String> configMap;
    private String arthasHome;
    private final boolean slientInit;
    private Instrumentation instrumentation;
    private Class<?> bootstrapClass;

    /**
     * 是否已经初始化，调用{@link #init()}会置为true，
     * 再调用{@link #destroy()}会重置为false
     */
    private boolean initialized;

    /**
     * 在修改后保证了此处至少是有值的
     * <p>aggentId值的拼接规则：{@link SplicerUtils#join(String...)}
     * <p>容器内部署ip不会一致，port预防其他形式部署后同ip下重复的可能性
     */
    private final String agentId;

    public ArthasAgent(Map<String, String> configMap, String arthasHome, boolean slientInit,
                       Instrumentation instrumentation) {
        this.configMap = configMap;
        this.agentId = configMap.get(ArthasClientConstant.ARTHAS_PREFIX + ArthasClientConstant.AGENT_ID);
        this.arthasHome = arthasHome;
        this.slientInit = slientInit;
        this.instrumentation = instrumentation;
    }

    public Map<String, String> getConfigMap() {
        return new ConcurrentHashMap<>(configMap);
    }

    public String getAgentId() {
        return agentId;
    }

    public void init() throws IllegalStateException {
        if (initialized) {
            log.warn("Arthas已启动");
            return;
        }
        // 尝试判断arthas是否已在运行，如果是的话，直接就退出
        try {
            Class.forName("java.arthas.SpyAPI"); // 加载不到会抛异常
            if (SpyAPI.isInited()) {
                return;
            }
        } catch (Throwable e) {
            // ignore
        }

        try {
            if (instrumentation == null) {
                this.instrumentation = ByteBuddyAgent.install();
            }
            // 检查 arthasHome
            if (arthasHome == null || arthasHome.trim().isEmpty()) {
                // 解压出 arthasHome
                URL coreJarUrl = this.getClass().getClassLoader().getResource("arthas-bin.zip");
                if (coreJarUrl != null) {
                    File tempArthasDir = createTempDir();
                    ZipUtil.unpack(coreJarUrl.openStream(), tempArthasDir);
                    arthasHome = tempArthasDir.getAbsolutePath();
                } else {
                    throw new IllegalArgumentException("can not getResources arthas-bin.zip from classloader: "
                            + this.getClass().getClassLoader());
                }
            }

            // find arthas-core.jar
            File arthasCoreJarFile = new File(arthasHome, ARTHAS_CORE_JAR);
            if (!arthasCoreJarFile.exists()) {
                throw new IllegalStateException("can not find arthas-core.jar under arthasHome: " + arthasHome);
            }
            AttachArthasClassloader arthasClassLoader = new AttachArthasClassloader(
                    new URL[]{arthasCoreJarFile.toURI().toURL()});

            /**
             * <pre>
             * ArthasBootstrap bootstrap = ArthasBootstrap.getInstance(inst);
             * </pre>
             */
            bootstrapClass = arthasClassLoader.loadClass(ARTHAS_BOOTSTRAP);
            Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class, Map.class).invoke(null,
                    instrumentation, configMap);
            boolean isBind = (Boolean) bootstrapClass.getMethod(IS_BIND).invoke(bootstrap);
            if (!isBind) {
                String errorMsg = "Arthas server port binding failed! Please check $HOME/logs/arthas/arthas.log for more details.";
                throw new RuntimeException(errorMsg);
            }
            initialized = true;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void destroy() {
        if (!initialized) {
            log.warn("未初始化的Arthas，不执行销毁动作");
        }
        try {
            Object bootstrap = bootstrapClass.getMethod(GET_INSTANCE, Instrumentation.class, Map.class).invoke(null,
                    instrumentation, configMap);
            bootstrapClass.getMethod(DESTROY).invoke(bootstrap);
        } catch (Throwable e) {
            log.error("Arthas销毁异常，异常信息：" + e);
            errorMessage = e.getMessage();
            if (!slientInit) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static File createTempDir() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        String baseName = "arthas-" + System.currentTimeMillis() + "-";

        for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
            File tempDir = new File(baseDir, baseName + counter);
            if (tempDir.mkdir()) {
                return tempDir;
            }
        }
        throw new IllegalStateException("Failed to create directory within " + TEMP_DIR_ATTEMPTS + " attempts (tried "
                + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
