package com.hmall.service;

import com.hmall.domain.dto.ItemDTO;
import com.hmall.domain.dto.OrderDetailDTO;
import com.hmall.domain.po.Item;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 */
public interface IItemService extends IService<Item> {

    void deductStock(List<OrderDetailDTO> items);

    List<ItemDTO> queryItemByIds(Collection<Long> ids);
}
