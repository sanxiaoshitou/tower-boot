package com.hxl.starter.controller;

import com.hxl.starter.model.ApiResult;
import com.hxl.starter.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author hxl
 * @Description
 * @Date 2023/11/28 22:22
 **/
@RestController
@RequestMapping("/bd")
public class DbController {

    @Autowired
    private UserService userService;

    @GetMapping("/findTest")
    @ApiOperation(value = "findTest")
    public ApiResult<Long> findTest() {
        return ApiResult.success(userService.getUserId());
    }
}
