package com.usian.controller;

import com.usian.pojo.TbItemParam;
import com.usian.service.ItemParamService;
import com.usian.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service/itemParam")
public class ItemParamController {
    @Autowired
    private ItemParamService itemParamService;
    @RequestMapping("/insertItemParam")
    public Integer insertItemParam(Long itemCatId,String paramData){
        return itemParamService.insertItemParam(itemCatId,paramData);
    }

    @RequestMapping("deleteItemParamById")
    public void deleteItemParamById(Long id){
        itemParamService.deleteItemParamById(id);
    }

    @RequestMapping("selectItemParamAll")
    public PageResult selectItemParamAll(Integer page, Integer rows){
        return itemParamService.selectItemParamAll(page,rows);
    }

    @RequestMapping("/selectItemParamByItemCatId/{itemCatId}")
    public TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") long itemCatId){
        return itemParamService.selectItemParamByItemCatId(itemCatId);
    }
}
