package com.hxl.starter.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Author hxl
 * @description
 * @Date 2023-11-13 14:25
 **/
@Controller("/")
public class IndexController {

    @GetMapping("/")
    @ApiOperation(value = "默认界面")
    public String index() {
        return "index";
    }


    /**
     * 返回页面
     *
     * @param v       页面地址
     * @param request 请求
     * @return 页面
     */
    @GetMapping("/p")
    @ApiOperation(value = "默认跳转界面")
    public String view(@RequestParam String v, HttpServletRequest request) {
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            request.setAttribute(name, request.getParameter(name));
        }
        return v;
    }

}
