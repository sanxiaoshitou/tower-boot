package com.hxl.arthas.utils;

public class SplicerUtils {

    public static final String MEDIUM = "_";

    /**
     * 每个param间隔会拼接{@link #MEDIUM}
     *
     * @param params 参数
     * @return 成品
     */
    public static String join(String... params) {
        if (params == null || params.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (String param : params) {
            result.append(param).append(MEDIUM);
        }
        return result.substring(0, result.length() - MEDIUM.length());
    }

    /**
     * 从{@code param}中截取某段（前提是param为{@link #join(String...)}同规则拼接产物）
     *
     * @param param 参数
     * @param index 第几段（从0开始）
     * @return 该段的内容
     */
    public static String parse(String param, int index) {
        if (param == null || "".equals(param)) {
            return "";
        }
        return param.split(MEDIUM)[index];
    }
}
