package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DbTest {

    //1、根据id查询用户
    @Test
    public void testQueryById() {
        User user = Db.getById(1L, User.class);
        System.out.println(user);
    }

    //2、查询名字中包含o且余额大于等于1000的用户；
    @Test
    public void testQueryByName() {
        List<User> userList = Db.lambdaQuery(User.class)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000)
                .list();
        for (User user : userList) {
            System.out.println(user);
        }
    }

    //3、更新用户名为Rose的余额为2000
    @Test
    public void testUpdate() {
        Db.lambdaUpdate(User.class)
                .set(User::getBalance, 2000)
                .eq(User::getUsername, "Rose")
                .update();
    }
}
