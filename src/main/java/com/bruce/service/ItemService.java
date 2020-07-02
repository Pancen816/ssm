package com.bruce.service;

import com.bruce.entity.Item;
import com.github.pagehelper.PageInfo;

/**
 * 商品模块的 service 接口
 *
 * @Project ssm
 * @Package com.bruce.service
 * @ClassName ItemService
 * @Author Bruce
 * @Date 2020/6/29 19:22
 * @Version 1.0
 **/
public interface ItemService {

    // 根据商品名称分页查询商品信息
    PageInfo<Item> findProductByNameLimit(String name,Integer page,Integer size);

    // 添加商品
    void save(Item item);

    // 根据id删除商品
    void deleteById(Integer id);

    // 根据id查询指定商品信息
    Item findById(Integer id);

    // 修改商品
    void updateItem(Item item);

}
