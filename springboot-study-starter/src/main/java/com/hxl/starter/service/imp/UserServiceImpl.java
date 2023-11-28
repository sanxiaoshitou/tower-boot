package com.hxl.starter.service.imp;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hxl.starter.mapper.UserMapper;
import com.hxl.starter.domain.po.UserPo;
import com.hxl.starter.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Author hxl
 * @Date 2023/11/25 22:01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPo> implements UserService {

    @Override
    public Long getUserId() {
        LambdaQueryWrapper<UserPo> wrapper = Wrappers.lambdaQuery();
        List<UserPo> list = list(wrapper);
        return CollectionUtil.isNotEmpty(list) ? list.get(0).getId() : 0L;
    }
}
