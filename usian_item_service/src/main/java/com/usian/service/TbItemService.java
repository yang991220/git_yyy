package com.usian.service;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParamItem;
import com.usian.utils.PageResult;
import com.usian.vo.ItemVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


public interface TbItemService {


    TbItem selectItemInfo(Long id);

   PageResult selectTbItemAllByPage(@RequestParam(name = "page") Integer page, @RequestParam(name = "rows")Integer rows);

    void insertTbItem(ItemVo itemVo);

    Map<String, Object> preUpdateItem(Long itemId);

    void updateTbItem(ItemVo itemVo);

    void deleteItemById(Long itemId);

    TbItemParamItem selectTbItemParamItemByItemId(Integer itemId);

    TbItemDesc selectItemDescByItemId(Long itemId);
}
