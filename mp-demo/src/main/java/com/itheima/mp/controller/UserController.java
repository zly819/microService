package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("用户接口管理")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiOperation("新增用户")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userFormDTO) {

        //转换为user
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        userService.save(user);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.removeById(id);
    }

    @ApiOperation("根据id查询用户")
    @GetMapping("/{id}")
    public UserVO queryById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return BeanUtil.copyProperties(user, UserVO.class);
    }

    @ApiOperation("根据id批量查询用户")
    @GetMapping
    public List<UserVO> queryByIds(@RequestParam("ids") List<Long> ids) {
        List<User> usersList = userService.listByIds(ids);

        return BeanUtil.copyToList(usersList, UserVO.class);
    }


}
