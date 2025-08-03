package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

    /**
     * 更新id为1,2,4的用户的余额，扣200
     *
     * UPDATE user SET balance = balance - 1 where id in(1,2,4)
     */
    @Test
    public void testUpdateWrapper() {

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();

        updateWrapper.setSql("balance = balance - 200");
        //条件
        updateWrapper.in("id",1, 2, 4);

        userMapper.update(null,updateWrapper);
    }

    /**
     * 查询出名字中带o的，存款大于等于1000元的人（id,username,info,balance）
     */
    @Test
    public void testLambdaQueryWrapper() {
        //1、构造查询条件；构造 where username like 'o' and balance >= 1000
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.
                select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        //2、查询
        List<User> list = userMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }

}
