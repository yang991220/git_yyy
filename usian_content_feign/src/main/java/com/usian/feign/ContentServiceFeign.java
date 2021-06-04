package com.usian.feign;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.PageResult;
import com.usian.vo.BigADContentVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "usian-content-service")
public interface ContentServiceFeign {

    //查询首页打广告信息
    @RequestMapping("/service/content/selectFrontendContentByAD")
    public  List<BigADContentVo> selectFrontendContentByAD();

    //删除内容类别
    @RequestMapping("/service/content/deleteContentCategoryById")
    public void deleteContentCategoryById(@RequestParam("categoryId") Long categoryId);
    //删除内容
    @RequestMapping("/service/content/deleteContentByIds")
    public void deleteContentByIds (@RequestParam("ids") Long ids);

    //修改内容类别
    @RequestMapping("/service/content/updateContentCategory")
    public void updateContentCategory(@RequestParam("parentId") Long id , @RequestParam("name") String name);

    //添加内类类别
    @RequestMapping("/service/content/insertContentCategory")
    public void insertContentCategory(@RequestParam("parentId") Long parentId , @RequestParam("name") String name);


    //查询内容分类表
    @RequestMapping("/service/content/selectContentCategoryByParentId")
    List<TbContentCategory> selectContentCategoryByParentId(@RequestParam("id") Long id);


    //查询内容接口
    @PostMapping("/service/content/selectTbContentAllByCategoryId")
    PageResult selectTbContentAllByCategoryId(@RequestParam("page") Integer page,
                                              @RequestParam("rows") Integer rows,
                                              @RequestParam("categoryId") Long categoryId);


    //添加内容
    @RequestMapping("/service/content/insertTbContent")
    public Integer insertTbContent( TbContent tbContent);


}
