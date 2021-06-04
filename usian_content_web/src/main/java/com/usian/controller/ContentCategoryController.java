package com.usian.controller;

import com.usian.feign.ContentServiceFeign;
import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/content")
public class ContentCategoryController {
    @Autowired
    private ContentServiceFeign contentServiceFeign;

    //删除内容
    @RequestMapping("/deleteContentByIds")
    public Result deleteContentByIds (@RequestParam("ids") Long ids){
        try {
            contentServiceFeign.deleteContentByIds(ids);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    //添加内容
    @RequestMapping("/insertTbContent")
    public Result insertTbContent(TbContent tbContent){
        Integer num = contentServiceFeign.insertTbContent(tbContent);
        if (num!=null){
            return Result.ok();
        }
        return Result.error("添加失败");
    }

    //内容查询
    @RequestMapping("/selectTbContentAllByCategoryId")
    public Result selectTbContentAllByCategoryId(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "30") Integer rows, Long categoryId) {
        PageResult pageResult = contentServiceFeign.selectTbContentAllByCategoryId(page, rows, categoryId);
        if (pageResult != null) {
            return Result.ok(pageResult);
        }
        return Result.error("查无结果");
    }



    //删除内容类别
    @RequestMapping("/deleteContentCategoryById")
    public Result deleteContentCategoryById(@RequestParam("categoryId") Long categoryId){
        try {
            contentServiceFeign.deleteContentCategoryById(categoryId);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }
    //修改内容类别
    @RequestMapping("/updateContentCategory")
    public Result updateContentCategory(@RequestParam("id") Long id , @RequestParam("name") String name){
        try {
            contentServiceFeign.updateContentCategory(id,name);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }



    //添加内类类别
    @RequestMapping("/insertContentCategory")
    public Result insertContentCategory(@RequestParam("parentId") Long parentId , @RequestParam("name") String name){
        try {
            contentServiceFeign.insertContentCategory(parentId,name);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    //查看内容类别
    @RequestMapping("/selectContentCategoryByParentId")
    public Result selectContentCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") Long id){
        List<TbContentCategory> tbItemCatList = contentServiceFeign.selectContentCategoryByParentId(id);
        return Result.ok(tbItemCatList);
    }

}
