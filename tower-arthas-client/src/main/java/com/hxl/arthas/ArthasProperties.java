package com.hxl.arthas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hxl.arthas.constant.ArthasClientConstant;
import com.hxl.arthas.utils.GetRealLocalIP;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.hxl.arthas.constant.ArthasClientConstant.DEFAULT_FORBIDDEN;

@ConfigurationProperties(prefix = ArthasProperties.PREFIX)
@Data
@Slf4j
public class ArthasProperties {

    public static final String PREFIX = "tower.arthas";

    /**
     * TODO 只是为"ip+port"的agentId唯一性标记，不作功能使用
     * 如果由于环境复杂导致无法自动获取，请自设
     */
    private String agentIp;

    /**
     * 默认取${server.port}没有则为{@link ArthasClientConstant.DEFAULT_PORT}，如果不想按此标准，请自设
     */
    private String agentPort;

    /**
     * 用作服务端的ip（代指本机接收命令，自己作为服务端），默认localhost
     */
    private String ip;

    /**
     * 如果配置 telnetPort为 -1 ，则不listen telnet端口。httpPort类似。
     *
     * <p>如果配置 telnetPort为 0 ，则随机telnet端口，
     * 在~/logs/arthas/arthas.log里可以找到具体端口日志。httpPort类似</>
     */
    private int telnetPort;

    /**
     * @see #telnetPort 规则看此变量注释
     */
    private int httpPort;

    /**
     * 应用名，默认会取${spring.application.name}
     */
    private String appName;

    /**
     * 解决<href =https://github.com/alibaba/arthas/issues/1838></href>
     */
    private boolean tryGc = true;

    private boolean tryRemove = false;

    /**
     * tunnelServer服务器地址
     * 例：'ws://127.0.0.1:7777/ws' 端口运维会域名映射，研发无需考虑
     */
    private String tunnelServer;

    /**
     * report executed command
     */
    private String statUrl;

    /**
     * session timeout seconds
     */
    private long sessionTimeout;

    private String home;

    /**
     * when arthas agent init error will throw exception by default.
     */
    private boolean slientInit = true;

    /**
     * 需要禁用的命令行，例如（stop）等，含有多个则“,”分割
     */
    private String disabledCommands;

    @JsonIgnore
    @Resource
    private Environment environment;

    @PostConstruct
    public void init() {
        if (StringUtils.hasLength(getDisabledCommands())) {
            setDisabledCommands(DEFAULT_FORBIDDEN + "," + getDisabledCommands());
        } else {
            setDisabledCommands(DEFAULT_FORBIDDEN);
        }
        if (agentIp == null) {
            agentIp = GetRealLocalIP.getLocalIP();
        }
        if (agentPort == null) {
            agentPort = environment.getProperty("server.port", ArthasClientConstant.DEFAULT_PORT);
        }
    }
}
