package com.usian.feign;

import com.usian.pojo.*;
import com.usian.utils.PageResult;
import com.usian.utils.Result;
import com.usian.vo.ItemCategoryDataVo;
import com.usian.vo.ItemVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public class ItemFeignFallBack implements TbItemFeign{
    @Override
    public TbItemDesc selectItemDescByItemId(Long itemId) {
        return null;
    }

    @Override
    public TbItemParamItem selectTbItemParamItemByItemId(Integer itemId) {
        return null;
    }

    @Override
    public ItemCategoryDataVo selectItemCategoryAll() {
        return null;
    }

    @Override
    public Integer insertItemParam(Long itemCatId, String paramData) {
        return null;
    }

    @Override
    public void deleteItemParamById(Long id) {

    }

    @Override
    public PageResult selectItemParamAll(Integer page, Integer rows) {
        return null;
    }

    @Override
    public Result deleteItemById(Long itemId) {
        return null;
    }

    @Override
    public Result updateTbItem(ItemVo itemVo) {
        return null;
    }

    @Override
    public Map<String, Object> preUpdateItem(Long itemId) {
        return null;
    }

    @Override
    public Result insertTbItem(ItemVo itemVo) {
        return null;
    }

    @Override
    public TbItemParam selectItemParamByItemCatId(Long itemCatId) {
        return null;
    }

    @Override
    public List<TbItemCat> selectItemCategoryByParentId(long id) {
        return null;
    }

    @Override
    public PageResult selectTbItemAllByPage(Integer page, Integer rows) {
        return null;
    }

    @Override
    public TbItem selectItemInfo(Long id) {
        return null;
    }
}
