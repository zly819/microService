package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ServiceUserImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public void deductBalance(Long id, Integer amount) {
        //1、查询用户
        User user = this.getById(id);
        //2、判断状态
        if (user == null || user.getStatus() == UserStatus.FREEZE) {
            throw new RuntimeException("用户状态异常");
        }
        //3、判断余额
        if (user.getBalance() < amount) {
            throw new RuntimeException("用户余额不足");
        }
        //4、扣减余额
//        baseMapper.deductBalance(id, amount);

        //获取扣减之后的余额
        int remainBalance = user.getBalance() - amount;

        this.lambdaUpdate()
                .set(User::getBalance, remainBalance)//设置余额
                //当余额为0的时候，用户状态修改为2
                .set(remainBalance == 0, User::getStatus, 2)
                .eq(User::getId, id)//条件
                .update();
    }

    @Override
    public UserVO queryUserAndAddressById(Long id) {
        //1.查询用户信息
        User user = getById(id);
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);

        //2.根据用户id查询其对应的用户地址列表；然后转换为vo列表设置回userVO中
        List<Address> addressList = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, user.getId())
                .list();
        List<AddressVO> addressVOS = BeanUtil.copyToList(addressList, AddressVO.class);
        userVO.setAddresses(addressVOS);
        return userVO;
    }

    @Override
    public List<UserVO> queryUserAndAddressByIds(List<Long> ids) {
        //1.根据id集合查询用户列表
        List<User> userList = listByIds(ids);
        List<UserVO> userVOList = BeanUtil.copyToList(userList, UserVO.class);
        //2.根据用户id集合查询这些用户对应的地址列表---->如果一个用户有2个地址的话，会有两条记录在下面集合里面
        List<Address> addressList = Db.lambdaQuery(Address.class)
                .in(Address::getUserId, ids)
                .list();
        List<AddressVO> addressVOS = BeanUtil.copyToList(addressList, AddressVO.class);

        //希望将上面的列表转换为map<用户id，用户地址列表>
        Map<Long, List<AddressVO>> userAddressMap = addressVOS.stream().collect(Collectors.groupingBy(AddressVO::getUserId));

        //就可以循环用户列表，然后根据用户id从map中获取对应的地址列表
        for (UserVO userVO : userVOList) {
            List<AddressVO> addressVOList = userAddressMap.get(userVO.getId());
            userVO.setAddresses(addressVOList);
        }

        return userVOList;
    }
}
