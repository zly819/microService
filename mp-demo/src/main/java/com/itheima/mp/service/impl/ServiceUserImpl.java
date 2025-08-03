package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class ServiceUserImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductBalance(Long id, Integer amount) {
        //1、查询用户
        User user = this.getById(id);
        //2、判断状态
        if (user == null || user.getStatus() == 2) {
            throw new RuntimeException("用户状态异常");
        }
        //3、判断余额
        if (user.getBalance() < amount) {
            throw new RuntimeException("用户余额不足");
        }
        //4、扣减余额
        baseMapper.deductBalance(id, amount);
    }
}
