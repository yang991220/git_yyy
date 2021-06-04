package com.usian.controller;

import com.usian.feign.TbItemFeign;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frontend/detail")
public class DetailController {

    @Autowired
    private TbItemFeign tbItemFeign;

    @RequestMapping("/selectItemInfo")
    public Result selectItemInfo(@RequestParam("itemId") Long itemId){
        try {
            TbItem tbItem = tbItemFeign.selectItemInfo(itemId);
            return Result.ok(tbItem);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping("/selectTbItemParamItemByItemId")
    public Result selectTbItemParamItemByItemId(@RequestParam("itemId") Integer itemId){
        try {
            TbItemParamItem tbItemParamItem = tbItemFeign.selectTbItemParamItemByItemId(itemId);
            return Result.ok(tbItemParamItem);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @RequestMapping("/selectItemDescByItemId")
    public Result selectItemDescByItemId(@RequestParam("itemId") Long itemId){
        try {
            TbItemDesc tbItemDesc = tbItemFeign.selectItemDescByItemId(itemId);
            return Result.ok(tbItemDesc);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

}
