package com.usian.controller;

import com.usian.pojo.TbItemCat;
import com.usian.service.TbItemCatService;
import com.usian.vo.ItemCategoryDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/itemCategory")
public class TbItemCatCiontroller {

    @Autowired
    private TbItemCatService tbItemCatService;


    //返回首页需要的所有商品的分类信息
    @RequestMapping("/selectItemCategoryAll")
    public ItemCategoryDataVo selectItemCategoryAll(){
       return tbItemCatService.selectItemCategoryAll();
    }


    /**
     * 根据父类母ID 查询所有的当前的孩子节点
     * @param id
     * @return
     */
    @RequestMapping("/selectItemCategoryByParentId")
    public List<TbItemCat> selectItemCategoryByParentId(@RequestParam("id") long id){
        return tbItemCatService.selectItemCategoryByParentId(id);

    }
}
