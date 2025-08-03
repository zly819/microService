package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
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

    /**
     * 根据用户Id、金额扣减用户的余额
     * @param id 用户id
     * @param amount 金额
     */
    @ApiOperation("根据用户Id、金额扣减用户的余额")
    @PutMapping("/{id}/deduction/{amount}")
    public void updateBalanceById(@PathVariable("id") Long id, @PathVariable("amount") Integer amount) {
        userService.deductBalance(id, amount);
    }


    @ApiOperation("根据查询条件userQuery 查询用户列表")
    @PostMapping("/list")
    public List<UserVO> queryList(@RequestBody UserQuery userQuery){
        String userName = userQuery.getName();
        Integer status = userQuery.getStatus();
        Integer minBalance = userQuery.getMinBalance();
        Integer maxBalance = userQuery.getMaxBalance();

        List<User>  userList = userService.lambdaQuery()
                .like(StrUtil.isNotBlank(userName), User::getUsername,userName)
                .eq(status != null, User::getStatus, status)
                .ge(minBalance != null, User::getBalance, minBalance)
                .le(maxBalance != null, User::getBalance, maxBalance)
                .list();//最终进行查询
        return BeanUtil.copyToList(userList, UserVO.class);
    }














}
