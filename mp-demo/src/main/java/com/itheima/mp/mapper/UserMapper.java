package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    @Update("update mp.user set balance = balance - #{amount} ${ew.customSqlSegment}")
    void updateBalanceByWrapper(@Param("amount") int amount, @Param("ew") QueryWrapper<User> queryWrapper);

}
