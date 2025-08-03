package com.hmall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.exception.BadRequestException;
import com.hmall.common.exception.BizIllegalException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.common.utils.CollUtils;
import com.hmall.common.utils.UserContext;
import com.hmall.domain.dto.CartFormDTO;
import com.hmall.domain.dto.ItemDTO;
import com.hmall.domain.po.Cart;
import com.hmall.domain.po.Item;
import com.hmall.domain.vo.CartVO;
import com.hmall.mapper.CartMapper;
import com.hmall.service.ICartService;
import com.hmall.service.IItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {

    private final IItemService itemService;

    @Override
    public void addItem2Cart(CartFormDTO cartFormDTO) {
        //TODO 如果商品已经添加过则购买数量添加即可；如果未添加过则新增一条购物车记录。一个用户最多放置购物车商品为10
        if(checkItemExists(cartFormDTO.getItemId(), UserContext.getUser())) {
            //1、添加过该商品，购买数量+1
            lambdaUpdate().setSql("num = num + 1")
                    .eq(Cart::getItemId, cartFormDTO.getItemId())
                    .eq(Cart::getUserId, UserContext.getUser())
                    .update();
        } else {
            //2、未添加过该商品，如果当前这个用户的购物车商品没有超过10个的话；可以新增一条购物车记录
            Long count = lambdaQuery().eq(Cart::getUserId, UserContext.getUser()).count();
            if (count >= 10) {
                throw new BizIllegalException("购物车商品数量超过限制！");
            }
            Cart cart = BeanUtils.copyProperties(cartFormDTO, Cart.class);
            cart.setUserId(UserContext.getUser());
            save(cart);
        }
    }


    @Override
    public List<CartVO> queryMyCarts() {
       //TODO 查询当前登录用户的购物车列表；需要将Cart转换为CartVO；且CartVO中需要包含商品的最新价格、状态、库存等信息。
        //1、查询当前登录用户的购物车列表
        List<Cart> cartList = lambdaQuery().eq(Cart::getUserId, UserContext.getUser()).list();
        if(!CollUtils.isEmpty(cartList)){
            List<CartVO> cartVOList = BeanUtils.copyToList(cartList, CartVO.class);

            //2、设置商品的最新价格、状态、库存等信息
            //2.1、收集商品id集合
            List<Long> itemIdList = cartVOList.stream().map(CartVO::getItemId).collect(Collectors.toList());

            //2.2、根据商品id集合批量查询商品
            Map<Long, Item> itemMap = itemService.listByIds(itemIdList).stream().collect(Collectors.toMap(Item::getId, Function.identity()));

            //2.3、遍历每个购物车商品，设置商品属性
            cartVOList.forEach(cartVO -> {
                Item item = itemMap.get(cartVO.getItemId());
                cartVO.setPrice(item.getPrice());
                cartVO.setStatus(item.getStatus());
                cartVO.setStock(item.getStock());
            });

            return cartVOList;
        }

        return CollUtils.emptyList();
    }

    @Override
    public void removeByItemIds(Collection<Long> itemIds) {
        // 1.构建删除条件，userId和itemId
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
        queryWrapper.lambda()
                .eq(Cart::getUserId, UserContext.getUser())
                .in(Cart::getItemId, itemIds);
        // 2.删除
        remove(queryWrapper);
    }

    private boolean checkItemExists(Long itemId, Long userId) {
        Long count = lambdaQuery().eq(Cart::getItemId, itemId)
                .eq(Cart::getUserId, userId)
                .count();
        return count>0;
    }
}
