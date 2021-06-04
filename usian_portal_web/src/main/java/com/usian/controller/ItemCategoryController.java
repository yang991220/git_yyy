package com.usian.controller;

import com.usian.feign.TbItemFeign;
import com.usian.utils.Result;
import com.usian.vo.ItemCategoryDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/itemCategory")
public class ItemCategoryController {
    @Autowired
    private TbItemFeign tbItemFeign;
    //返回首页需要的所有商品的分类信息

    @RequestMapping("/selectItemCategoryAll")
    public Result selectItemCategoryAll(){
        try {
            ItemCategoryDataVo dataVo =  tbItemFeign.selectItemCategoryAll();
            return Result.ok(dataVo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }



}
