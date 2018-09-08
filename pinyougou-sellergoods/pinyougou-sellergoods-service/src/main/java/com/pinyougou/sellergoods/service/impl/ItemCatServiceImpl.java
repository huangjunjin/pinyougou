package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.ItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(interfaceClass = ItemCatService.class)
public class ItemCatServiceImpl extends BaseServiceImpl<TbItemCat> implements ItemCatService {

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbItemCat itemCat) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbItemCat.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(itemCat.get***())){
            criteria.andLike("***", "%" + itemCat.get***() + "%");
        }*/

        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        PageInfo<TbItemCat> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void deleteItemCat(Long[] ids) {
        //本级传过来的id数组遍历
        for (Long id : ids) {
            //根据id删除
            deleteById(id);
        }
    }

    @Override
    public void deleteById(Long id) {
        //把本级的删除
        itemCatMapper.deleteByPrimaryKey(id);

        //创建一个对象 根据对象把下一级的List找出来
        TbItemCat itemCat = new TbItemCat();
        itemCat.setParentId(id);
        List<TbItemCat> itemCatList = itemCatMapper.select(itemCat);
        //判断是否为空 空就是没有下一级 停止递归
        if (itemCatList!=null&&itemCatList.size()>0){
            //把每个tbItemCat遍历出来
            for (TbItemCat tbItemCat : itemCatList) {
                //递归删除 看还有没有下一级
                deleteById(tbItemCat.getId());
            }
        }
    }

}
