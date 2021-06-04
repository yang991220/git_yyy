package com.usian.controller;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.service.ContentCategoryService;
import com.usian.utils.PageResult;
import com.usian.vo.BigADContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/content")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    //查询首页打广告信息
    @RequestMapping("/selectFrontendContentByAD")
    public  List<BigADContentVo> selectFrontendContentByAD(){
       return contentCategoryService.selectFrontendContentByAD();
    }

    //删除内容类别
    @RequestMapping("/deleteContentCategoryById")
    public void deleteContentCategoryById(@RequestParam("categoryId") Long categoryId){
        contentCategoryService.deleteContentCategoryById(categoryId);
    }

    //删除内容
    @RequestMapping("/deleteContentByIds")
    public void deleteContentByIds (@RequestParam("ids") Long ids){
        contentCategoryService.deleteContentByIds(ids);
    }

    //添加内容
    @RequestMapping("/insertTbContent")
    public Integer insertTbContent(@RequestBody TbContent tbContent){
       return contentCategoryService.insertTbContent(tbContent);
    }

    //查询内容接口
    @RequestMapping("/selectTbContentAllByCategoryId")
    public PageResult selectTbContentAllByCategoryId(@RequestParam Integer page, @RequestParam Integer rows, @RequestParam Long categoryId) {
        return this.contentCategoryService.selectTbContentAllByCategoryId(page, rows, categoryId);
    }

    //修改内容类别
    @RequestMapping("/updateContentCategory")
    public void updateContentCategory(@RequestParam("parentId") Long id , @RequestParam("name") String name){
        contentCategoryService.updateContentCategory(id,name);
    }

    //添加内类类别
    @RequestMapping("/insertContentCategory")
    public void insertContentCategory(@RequestParam("parentId") Long parentId , @RequestParam("name") String name){
        contentCategoryService.insertContentCategory(parentId,name);
    }


    /**
     * 根据父类母ID 查询所有的当前的孩子节点
     * @param id
     * @return
     */
    @RequestMapping("/selectContentCategoryByParentId")
    public List<TbContentCategory> selectContentCategoryByParentId(@RequestParam("id") Long id){
        return contentCategoryService.selectContentCategoryByParentId(id);

    }
}
