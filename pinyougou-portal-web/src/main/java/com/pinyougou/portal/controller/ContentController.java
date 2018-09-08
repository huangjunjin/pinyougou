package com.pinyougou.portal.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.pojo.TbContent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/content")
@RestController
public class ContentController {

    @Reference
    private ContentService contentService;

    /**
     * 根据categoryId查找content  轮播图pic
     * http://pinyougou.com/content/findContentListByCategoryId.do?categoryId=1
     */
    @GetMapping("/findContentListByCategoryId")
    public List<TbContent>findContentListByCategoryId(Long categoryId){
        return contentService.findContentListByCategoryId(categoryId);
    }


}
