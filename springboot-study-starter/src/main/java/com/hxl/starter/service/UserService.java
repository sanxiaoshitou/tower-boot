package com.hxl.starter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hxl.starter.domain.po.UserPo;

public interface UserService extends IService<UserPo> {

    Long getUserId();
}
