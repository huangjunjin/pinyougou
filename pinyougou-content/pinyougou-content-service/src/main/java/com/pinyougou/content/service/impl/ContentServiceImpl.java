package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceClass = ContentService.class)
public class ContentServiceImpl extends BaseServiceImpl<TbContent> implements ContentService {

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void add(TbContent tbContent){
        super.add(tbContent);
        //更新内容分类对应在redis中的内容列表缓存
        updateContentInRedisByCategoryId(tbContent.getCategoryId());
    }

    /**
     * 将分类id对应的redis数据删除
     * @param categoryId 分类id
     */
    private void updateContentInRedisByCategoryId(Long categoryId){
        redisTemplate.boundHashOps("content").delete(categoryId);
    }

    @Override
    public void update(TbContent tbContent) {
        super.update(tbContent);

        //查询原来这个内容对应的分类id
        TbContent oldContent = findOne(tbContent.getId());
        if (!oldContent.getCategoryId().equals(tbContent.getCategoryId())){
            //修改内容的时候已经修改过内容分类;所以要将原来分类的数据更新
            updateContentInRedisByCategoryId(oldContent.getCategoryId());
        }
        //更新当前对于的分类的redis数据
        updateContentInRedisByCategoryId(tbContent.getCategoryId());
    }

    @Override
    public void deleteByIds(Serializable[] ids){
        //查询所有的内容;并且要更新这些每一个内容对应的分类在redis中的缓存数据
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andIn("id", Arrays.asList(ids));

        //根据条件查询content
        List<TbContent> contentList = contentMapper.selectByExample(example);
        for (TbContent tbContent : contentList) {
            //更新缓存
            updateContentInRedisByCategoryId(tbContent.getCategoryId());
        }

        //删除
        super.deleteByIds(ids);
    }

    @Override
    public PageResult search(Integer page, Integer rows, TbContent content) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(content.get***())){
            criteria.andLike("***", "%" + content.get***() + "%");
        }*/

        List<TbContent> list = contentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<TbContent> findContentListByCategoryId(Long categoryId) {
        //先从缓存中查找
        List<TbContent>contentList= null;
        try {
            contentList= (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
            if (contentList!=null){
                return contentList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //根据内容分类id查询该分类下的所有有效内容并且按照排序字段降序排序
        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        //和categoryId一样的
        criteria.andEqualTo("categoryId",categoryId);
        //有效广告
        criteria.andEqualTo("status","1");

        //降序排序
        example.orderBy("sortOrder").desc();

        contentList = contentMapper.selectByExample(example);

        try {
            //设置缓存
            redisTemplate.boundHashOps("content").put(categoryId, contentList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contentList;
    }
}
