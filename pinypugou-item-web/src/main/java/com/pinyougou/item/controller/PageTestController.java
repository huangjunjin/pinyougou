package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/test")
@RestController
public class PageTestController {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemCatService itemCatService;

    //读取配置文件中的配置项
    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;

    /**
     * 模拟商品批量审核通过之后生成静态页面
     * @param goodsIds 商品spu id集合
     * @return 说明
     */
    @GetMapping("/audit")
    public String auditGoods(Long[] goodsIds){

        if (goodsIds != null && goodsIds.length > 0) {
            for (Long goodsId : goodsIds) {
                genHtml(goodsId);
            }
        }

        return "success";
    }

    /**
     * 模拟批量删除商品；并且删除商品对应生成在指定路径下的静态页面
     * @param goodsIds
     * @return
     */
    @GetMapping("/delete")
    public String deleteGoods(Long[] goodsIds){
        if (goodsIds != null && goodsIds.length > 0) {
            for (Long goodsId : goodsIds) {
                File file = new File(ITEM_HTML_PATH + goodsId + ".html");
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        return "success";
    }

    //生成商品静态页面
    private void genHtml(Long goodsId) {
        try {
            //模版
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");

            Map<String, Object> dataModel = new HashMap<>();

            //根据商品 id 查询商品基本、描述、启用的 SKU 列表
            Goods goods = goodsService.findGoodsByIdAndStatus(goodsId, "1");

            //goods 商品基本信息
            dataModel.put("goods", goods.getGoods());

            //goodsDesc 商品描述信息
            dataModel.put("goodsDesc", goods.getGoodsDesc());
            //itemList sku商品列表
            dataModel.put("itemList", goods.getItemList());
            //itemCat1 一级商品分类中文名称
            TbItemCat itemCat1 = itemCatService.findOne(goods.getGoods().getCategory1Id());
            dataModel.put("itemCat1", itemCat1.getName());
            //itemCat2 二级商品分类中文名称
            TbItemCat itemCat2 = itemCatService.findOne(goods.getGoods().getCategory2Id());
            dataModel.put("itemCat2", itemCat2.getName());
            //itemCat3 三级商品分类中文名称
            TbItemCat itemCat3 = itemCatService.findOne(goods.getGoods().getCategory3Id());
            dataModel.put("itemCat3", itemCat3.getName());

            //输出
            FileWriter fileWriter = new FileWriter(ITEM_HTML_PATH + goodsId + ".html");

            template.process(dataModel, fileWriter);

            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
 