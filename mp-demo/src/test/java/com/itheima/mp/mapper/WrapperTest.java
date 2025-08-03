package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class WrapperTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询出名字中带o的，存款大于等于1000元的人（id,username,info,balance）
     */
    @Test
    public void testQueryWrapper() {
        //创建条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //查询的列
        queryWrapper.select("id","username","info","balance");
        //查询条件：名字中带o的，存款大于等于1000元
        queryWrapper.like("username", "o");
        queryWrapper.ge("balance", 1000);
        //查询
        List<User> userList = userMapper.selectList(queryWrapper);
        //输出
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * 更新用户名为jack的用户的余额为2000
     */
    @Test
    public void testQueryWrapperUpdate() {

        User user = new User();
        user.setBalance(2000);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "jack");

        userMapper.update(user, queryWrapper);

    }
}
