package com.usian.service;

import com.usian.pojo.TbItemCat;
import com.usian.vo.ItemCategoryDataVo;

import java.util.List;


public interface TbItemCatService {



    List<TbItemCat> selectItemCategoryByParentId(long id);

    ItemCategoryDataVo selectItemCategoryAll();

}
