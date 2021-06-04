package com.usian.controller;

import com.usian.feign.TbItemFeign;
import com.usian.pojo.TbItemCat;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/backend/itemCategory")
public class TbItemCatController {
    @Autowired
    private TbItemFeign tbItemFeign;


    @RequestMapping("/selectItemCategoryByParentId")
    public Result selectItemCategoryByParentId(@RequestParam(name = "id",defaultValue = "0") long id){
        List<TbItemCat> tbItemCatList = tbItemFeign.selectItemCategoryByParentId(id);
        return Result.ok(tbItemCatList);
    }

}
