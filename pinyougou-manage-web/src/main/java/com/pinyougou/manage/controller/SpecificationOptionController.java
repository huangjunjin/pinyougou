package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationOptionService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/specificationOption")
public class SpecificationOptionController{

    @Reference
    private SpecificationOptionService specificationOptionService;

    /**
     * 查找所有
     */
    @RequestMapping("/findAll")
    public List<TbSpecificationOption> findAll(){
        return specificationOptionService.findAll();
    }

    /**
     * 根据分页查询
     */
    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value = "page",defaultValue = "1")Integer page,
                               @RequestParam(value = "rows",defaultValue = "10")Integer rows){
        return specificationOptionService.findPage(page,rows);
    }
    /**
     * 添加
     */
    @PostMapping("/add")
    public Result add(TbSpecificationOption specificationOption){
        try {
            specificationOptionService.add(specificationOption);
            return Result.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("添加失败");
    }
    /**
     * 根据id查找
     */
    @GetMapping("/findOne")
    public TbSpecificationOption findOne(Long id){
        return specificationOptionService.findOne(id);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(TbSpecificationOption specificationOption){
        try {
            specificationOptionService.update(specificationOption);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }
    /*
    删除
     */
    @GetMapping("/delete")
    public Result delete(Long[] ids){
        try {
            specificationOptionService.deleteByIds(ids);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 分页查询列表
     * @param specificationOption 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbSpecificationOption specificationOption, @RequestParam(value = "page", defaultValue = "1")Integer page,
                             @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return specificationOptionService.search(page, rows, specificationOption);
    }
}
