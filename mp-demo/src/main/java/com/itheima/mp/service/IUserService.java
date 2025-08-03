package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;

import java.util.List;

public interface IUserService extends IService<User> {


    /**
     * 根据用户Id、金额扣减用户的余额
     * @param id
     * @param amount
     */
    void deductBalance(Long id, Integer amount);

    /**
     * 根据用户id查询用户和地址
     * @param id
     * @return
     */
    UserVO queryUserAndAddressById(Long id);

    /**
     * 根据用户id查询用户和地址
     * @param ids
     * @return
     */
    List<UserVO> queryUserAndAddressByIds(List<Long> ids);
}
