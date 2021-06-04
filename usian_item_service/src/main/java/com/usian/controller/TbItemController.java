package com.usian.controller;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.service.TbItemService;
import com.usian.utils.PageResult;
import com.usian.vo.ItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/service/item")
public class TbItemController {

    @Autowired
    private TbItemService tbItemService;


    //查询商品的描述
    @RequestMapping("/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(@RequestParam("itemId") Long itemId) {
        return tbItemService.selectItemDescByItemId(itemId);
    }

    //查询商品对应的规格参数值
    @RequestMapping("/selectTbItemParamItemByItemId")
    public TbItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId") Integer itemId) {
        return tbItemService.selectTbItemParamItemByItemId(itemId);
    }

    //删除商品
    @RequestMapping("/deleteItemById")
    public void deleteItemById(@RequestParam("itemId") Long itemId) {
        tbItemService.deleteItemById(itemId);
    }

    //修改商品
    @RequestMapping("/updateTbItem")
    public void updateTbItem(@RequestBody ItemVo itemVo) {
        tbItemService.updateTbItem(itemVo);
    }


    //回显

    @RequestMapping("/preUpdateItem")
    public Map<String, Object> preUpdateItem(Long itemId) {
        return tbItemService.preUpdateItem(itemId);
    }

    //添加商品信息
    @RequestMapping("/insertTbItem")
    public void insertTbItem(@RequestBody ItemVo itemVo) {
        tbItemService.insertTbItem(itemVo);
    }

    /**
     * 查询商品信息
     * 根据商品id
     *
     * @param id
     * @return
     */
    @RequestMapping("/selectItemInfo")
    public TbItem selectItemInfo(Long id) {
        return tbItemService.selectItemInfo(id);
    }

    @RequestMapping("/selectTbItemAllByPage")
    public PageResult selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name = "rows") Integer rows) {
        return tbItemService.selectTbItemAllByPage(page, rows);
    }


}
