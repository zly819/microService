package com.hmall.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmall.common.domain.PageDTO;
import com.hmall.domain.dto.ItemDTO;
import com.hmall.domain.po.Item;
import com.hmall.domain.query.ItemPageQuery;
import com.hmall.service.IItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "搜索相关接口")
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final IItemService itemService;

    @ApiOperation("搜索商品")
    @GetMapping("/list")
    public PageDTO<ItemDTO> search(ItemPageQuery query) {
        // TODO 根据条件分页查询，默认根据更新时间降序排序
        LambdaQueryChainWrapper<Item> wrapper = itemService.lambdaQuery()
                .like(StrUtil.isNotBlank(query.getKey()), Item::getName, query.getKey())
                .eq(StrUtil.isNotBlank(query.getBrand()), Item::getBrand, query.getBrand())
                .eq(StrUtil.isNotBlank(query.getCategory()), Item::getCategory, query.getCategory())
                .ge(query.getMinPrice() != null, Item::getPrice, query.getMinPrice())
                .le(query.getMaxPrice() != null, Item::getPrice, query.getMaxPrice());
        if (query.getSortBy() !=null) {
            switch (query.getSortBy()){
                case "price":
                    wrapper.orderBy(true, query.getIsAsc(), Item::getPrice);
                    break;
                case "sold":
                    wrapper.orderBy(true, query.getIsAsc(), Item::getSold);
                    break;
                default:
                    wrapper.orderBy(true, query.getIsAsc(), Item::getUpdateTime);
                    break;
            }
        } else {
            wrapper.orderBy(true, query.getIsAsc(), Item::getUpdateTime);
        }
        Page<Item> itemPage = wrapper.page(new Page<>(query.getPageNo(), query.getPageSize()));

        return PageDTO.of(itemPage, ItemDTO.class);
    }
}
