package com.bruce.service.impl;

import com.bruce.entity.Item;
import com.bruce.enums.ExceptionInfoEnum;
import com.bruce.exception.SsmException;
import com.bruce.mapper.ItemMapper;
import com.bruce.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * ItemService接口的实现类
 *
 * @Project ssm
 * @Package com.bruce.service.impl
 * @ClassName ItemServiceImpl
 * @Author Bruce
 * @Date 2020/6/29 19:24
 * @Version 1.0
 **/
@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    /**
     * 根据条件分页查询商品信息
     * @author Bruce
     * @date 2020-06-30 15:48
     * @param name  商品名称
     * @param page  当前页
     * @param size  每页显示条数
     * @return com.github.pagehelper.PageInfo<com.bruce.entity.Item>
     */
    @Override
    public PageInfo<Item> findProductByNameLimit(String name, Integer page, Integer size) {
        // 分页助手开启分页
        PageHelper.startPage(page,size);
        // 封装查询条件
        Example example = new Example(Item.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(name)) {
            criteria.andLike("name", "%" + name + "%");
        }
        // 执行查询
        List<Item> list = itemMapper.selectByExample(example);
        // 封装成PageInfo
        PageInfo<Item> pageInfo = new PageInfo<>(list);
        // 响应数据
        return pageInfo;
    }

    /**
     * 添加商品
     * @author Bruce
     * @date 2020-06-30 15:47
     * @param item 商品对象
     * @return void
     */
    @Override
    @Transactional
    public void save(Item item) {
        int count = itemMapper.insertSelective(item);
        if (count != 1){
            // 添加失败
            log.error("【添加商品】添加商品失败！item = {}",item);
            throw new SsmException(ExceptionInfoEnum.ITEM_ADD_ERROR);
        }
    }

    /**
     * 根据id删除指定商品信息
     * @author Bruce
     * @date 2020-06-30 23:52
     * @param id  商品id
     * @return void
     */
    @Override
    @Transactional
    public void deleteById(Integer id) {

        int count = itemMapper.deleteByPrimaryKey(id);
        if (count != 1){
            // 删除失败
            log.error("【删除商品】删除商品失败！id = {}",id);
            throw new SsmException(ExceptionInfoEnum.ITEM_DELETE_ERROR);
        }
    }

    /**
     * 根据主键id查询商品
     * @author Bruce
     * @date 2020-07-01 01:49
     * @param id  商品id
     * @return com.bruce.entity.Item
     */
    @Override
    public Item findById(Integer id) {

        Item item = itemMapper.selectByPrimaryKey(id);

        return item;
    }

    @Override
    @Transactional
    public void updateItem(Item item) {
        int count = itemMapper.updateByPrimaryKeySelective(item);
        if (count != 1){
            // 修改失败
            log.error("【修改商品】修改商品失败！item = {}",item);
            throw new SsmException(ExceptionInfoEnum.ITEM_UPDATE_ERROR);
        }
    }

}
