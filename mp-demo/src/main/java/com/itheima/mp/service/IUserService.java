package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;

public interface IUserService extends IService<User> {


    /**
     * 根据用户Id、金额扣减用户的余额
     * @param id
     * @param amount
     */
    void deductBalance(Long id, Integer amount);
}
