package com.usian.service;

import com.usian.pojo.TbContent;
import com.usian.pojo.TbContentCategory;
import com.usian.utils.PageResult;
import com.usian.vo.BigADContentVo;

import java.util.List;

public interface ContentCategoryService {
    List<TbContentCategory> selectContentCategoryByParentId(Long id);

    void insertContentCategory(Long parentId, String name);

    void updateContentCategory(Long id, String name);

    PageResult selectTbContentAllByCategoryId(Integer page, Integer rows, Long categoryId);

    Integer insertTbContent(TbContent tbContent);

    void deleteContentByIds(Long ids);

    void deleteContentCategoryById(Long categoryId);

    List<BigADContentVo> selectFrontendContentByAD();

}
