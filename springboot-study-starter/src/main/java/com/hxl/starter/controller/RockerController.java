package com.hxl.starter.controller;

import com.hxl.rocker.model.RocketMsg;
import com.hxl.rocker.producer.RocketMessageProducer;
import com.hxl.starter.model.ApiResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/24 0:24
 */
@RestController
@RequestMapping("/rocker")
public class RockerController {

    @Autowired
    private RocketMessageProducer rocketMessageProducer;

    @GetMapping("/sendMessage")
    @ApiOperation(value = "sendMessage")
    public ApiResult<String> sendMessage() {

        RocketMsg rocketMsg = new RocketMsg();
        rocketMsg.setBody("hxl测试发送");

        rocketMessageProducer.sendMessage("PRODUCER_TOPIC", null, rocketMsg);
        return ApiResult.success();
    }

    @GetMapping("/sendDelayMessage")
    @ApiOperation(value = "sendDelayMessage")
    public ApiResult<String> sendDelayMessage() {

        RocketMsg rocketMsg = new RocketMsg();
        rocketMsg.setBody("延迟消息发送发送");

        rocketMessageProducer.sendMessage("DELAY_TOPIC", null, 5 * 60L, rocketMsg);
        return ApiResult.success();
    }
}
