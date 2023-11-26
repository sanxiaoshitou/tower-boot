package com.hxl.arthas.constant;

public interface ArthasClientConstant {

    String AGENT_ID = "agentId";

    String ARTHAS_PREFIX = "arthas.";

    String DEFAULT = "default";

    String DEFAULT_PORT = "0";

    /**
     * 默认禁用的命令
     */
    String DEFAULT_FORBIDDEN = "ognl,dump,heapdump,mc,retransform,redefine,tee,auth,stop";
}
