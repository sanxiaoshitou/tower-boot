package com.hxl.arthas;

import com.hxl.arthas.attach.ArthasAgent;
import com.hxl.arthas.attach.EnhanceArthasAgentInit;
import com.hxl.arthas.constant.ArthasClientConstant;
import com.hxl.arthas.utils.SplicerUtils;
import com.hxl.arthas.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


@ConditionalOnProperty(name = "spring.arthas.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ArthasProperties.class})
@Slf4j
public class ArthasConfiguration {

    @Autowired
    private ConfigurableEnvironment environment;

    /**
     * <pre>
     * 1. 提取所有以 arthas.* 开头的配置项，再统一转换为Arthas配置
     * 2. 避免某些配置在新版本里支持，但在ArthasProperties里没有配置的情况。
     * </pre>
     */
    @ConfigurationProperties(prefix = ArthasProperties.PREFIX)
    @ConditionalOnMissingBean(name = "arthasConfigMap")
    @Bean
    public HashMap<String, String> arthasConfigMap() {
        return new HashMap<String, String>();
    }

    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = ArthasProperties.PREFIX, name = "group", matchIfMissing = true)
    @Bean
    public ArthasAgent arthasAgent(@Autowired @Qualifier("arthasConfigMap") Map<String, String> arthasConfigMap,
                                   @Autowired ArthasProperties arthasProperties) {
        arthasConfigMap = StringUtils.removeDashKey(arthasConfigMap);
        /**
         * @see org.springframework.boot.context.ContextIdApplicationContextInitializer#getApplicationId(ConfigurableEnvironment)
         */
        String appName = environment.getProperty("APP_NAME");
        if (org.springframework.util.StringUtils.hasText(appName)) {
            arthasConfigMap.put("appName", appName);
        } else {
            appName = arthasConfigMap.get("appName");
            if (!org.springframework.util.StringUtils.hasLength(appName)) {
                appName = environment.getProperty("spring.application.name");
                arthasConfigMap.put("appName", appName);
            }
        }
        if (!org.springframework.util.StringUtils.hasLength(arthasConfigMap.get(ArthasClientConstant.AGENT_ID))) {
            String agentIp = environment.getProperty("POD_IP");
            if (!org.springframework.util.StringUtils.hasText(agentIp)) {
                agentIp = arthasProperties.getAgentIp();
            }
            String agentId = SplicerUtils.join(appName, agentIp, arthasProperties.getAgentPort());
            arthasConfigMap.put(ArthasClientConstant.AGENT_ID, agentId);
        }

        // 给配置全加上前缀
        Map<String, String> mapWithPrefix = new HashMap<>(arthasConfigMap.size());
        for (Entry<String, String> entry : arthasConfigMap.entrySet()) {
            mapWithPrefix.put(ArthasClientConstant.ARTHAS_PREFIX + entry.getKey(), entry.getValue());
        }

        final ArthasAgent arthasAgent = new ArthasAgent(mapWithPrefix, arthasProperties.getHome(),
                arthasProperties.isSlientInit(), null);
        log.info("Arthas agent instantiation success." + "【" + arthasAgent.getAgentId() + "】");
        new EnhanceArthasAgentInit(arthasAgent, arthasProperties).tryInit();
        return arthasAgent;
    }

//    @ConditionalOnMissingBean
//    @ConditionalOnBean(ArthasAgent.class)
//    @Bean
//    public ApplicationRegister applicationRegister(ArthasProperties properties, ArthasAgent arthasAgent) {
//        return new ApplicationRegister(properties, arthasAgent);
//    }
}
