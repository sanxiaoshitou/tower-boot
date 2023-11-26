package com.hxl.starter.service.imp;

import com.hxl.starter.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/25 22:01
 */
@Service
public class UserServiceImpl implements UserService {

    @Override
    public Long getUserId() {
        return 123L;
    }
}
