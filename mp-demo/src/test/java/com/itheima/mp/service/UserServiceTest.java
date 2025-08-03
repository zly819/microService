package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private IUserService userService;

    //批量插入
    @Test
    public void testSaveBatch(){
        long begin = System.currentTimeMillis();
        List<User> list = new ArrayList<>();
        for (int i = 1; i < 100000; i++) {
            list.add(buildUser(i));
            if (i % 1000 == 0){
                userService.saveBatch(list);
                list.clear();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - begin) + "ms");
    }

    private User buildUser(int i) {
        User user = new User();
        user.setUsername("user_"+i);
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
//        user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return user;
    }

    //测试分页
    @Test
    public void testPage(){
        //创建一个分页对象
        Page<User> page = new Page<>(2, 2);

        //分页查询 select * from user limit 2,2
        //limit 起始索引号，页大小
        Page<User> userPage = userService.page(page);

        //输出分页信息
        System.out.println("总页数：" + userPage.getPages());
        System.out.println("总记录数" + userPage.getTotal());
        for (User user : userPage.getRecords()) {
            System.out.println(user);
        }
    }
}
