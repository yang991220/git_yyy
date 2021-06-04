package com.usian.feign;

import com.usian.pojo.*;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemCategoryDataVo;
import com.usian.vo.ItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "usian-item-service" ,fallbackFactory = ItemFeignFallBackFactory.class )
//@FeignClient(value = "usian-item-service" )
public interface TbItemFeign {

    //查询商品的描述
    @RequestMapping("/service/item/selectItemDescByItemId")
    public TbItemDesc selectItemDescByItemId(@RequestParam("itemId") Long itemId);

    //查询商品对应的规格参数值
    @RequestMapping("/service/item/selectTbItemParamItemByItemId")
    public TbItemParamItem selectTbItemParamItemByItemId(@RequestParam("itemId") Integer itemId);

    //返回首页需要的所有商品的分类信息
    @RequestMapping("/service/itemCategory/selectItemCategoryAll")
    public ItemCategoryDataVo selectItemCategoryAll();
    //规格模板添加
    @RequestMapping("/service/itemParam/insertItemParam")
    Integer insertItemParam(@RequestParam("itemCatId") Long itemCatId,@RequestParam("paramData") String paramData);
    //规格模板删除
    @RequestMapping("/service/itemParam/deleteItemParamById")
    void deleteItemParamById(@RequestParam("id") Long id);

    //规格模板查询
    @RequestMapping("/service/itemParam/selectItemParamAll")
    PageResult selectItemParamAll(@RequestParam("page") Integer page,@RequestParam("rows") Integer rows);

    //删除商品
    @RequestMapping("/service/item/deleteItemById")
    public Result deleteItemById(@RequestParam("itemId") Long itemId);
    //修改商品
    @RequestMapping("/service/item/updateTbItem")
    public Result updateTbItem(@RequestBody ItemVo itemVo);

    //回显
    @RequestMapping("/service/item/preUpdateItem")
    public Map<String,Object> preUpdateItem(@RequestParam("itemId") Long itemId);
    //添加商品信息
    @RequestMapping("/service/item/insertTbItem")
    public Result insertTbItem(@RequestBody ItemVo itemVo);

    //查询商品规格参数模板接口
    @RequestMapping("/service/itemParam/selectItemParamByItemCatId/{itemCatId}")
    TbItemParam selectItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId);

    //查询类目信息
    @GetMapping("/service/itemCategory/selectItemCategoryByParentId")
    List<TbItemCat> selectItemCategoryByParentId(@RequestParam("id") long id);

    //查询所有商品信息
    @GetMapping("/service/item/selectTbItemAllByPage")
    PageResult selectTbItemAllByPage(@RequestParam("page") Integer page, @RequestParam("rows")Integer rows);

    @RequestMapping("/service/item/selectItemInfo")
    TbItem selectItemInfo(@RequestParam("id") Long id);




}
