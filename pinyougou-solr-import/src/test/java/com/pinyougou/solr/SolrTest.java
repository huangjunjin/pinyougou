package com.pinyougou.solr;

import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    //新增
    /*@Test
    public void testadd(){
        TbItem item = new TbItem();
        solrTemplate.saveBean(item);
        solrTemplate.commit();
    }*/

  /*  @Test
    public void testDeleteByQuery() {
        //根据条件删除；参数：查询表达式
        SimpleQuery query = new SimpleQuery("*:*");
        solrTemplate.delete(query);

        solrTemplate.commit();
    }*/

    /*@Test
    public void testDeleteById() {
        solrTemplate.deleteById("5424543");
        solrTemplate.commit();
    }*/

    //根据关键字分页查询
    @Test
    public void testQueryInPage(){
        SimpleQuery query = new SimpleQuery("*:*");
        query.setOffset(20);
        query.setRows(20);

        //多条件查询
        /*Criteria criteria1 = new Criteria("item_title").is("Mac");
        query.addCriteria(criteria1);*/

        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);

        System.out.println("总记录数为："+scoredPage.getTotalElements());
        System.out.println("总页数数为："+scoredPage.getTotalPages());

        for (TbItem item : scoredPage) {
            System.out.println("id=" + item.getId());
            System.out.println("title=" + item.getTitle());
            System.out.println("price=" + item.getPrice());
            System.out.println("image=" + item.getImage());
            System.out.println("更新时间=" + item.getUpdateTime());
        }
    }

    //多条件查询
   /* @Test
    public void testMultiQuery(){
        SimpleQuery query = new SimpleQuery();

        //参数1：在schema.xml文件中对应的编写了的域名；contains表示查询该域包含相应关键字的那些文档是不会分词的；is会分词
        Criteria criteria1 = new Criteria("item_title").is("魅族");
        query.addCriteria(criteria1);

        Criteria criteria2 = new Criteria("item_price").greaterThanEqual(500);
        query.addCriteria(criteria2);


        //查询；参数1：查询对象，参数2：是返回结果中的每个文档封装的实体类
        ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);

        showMsg(scoredPage);
    }*/

}
