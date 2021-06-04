package com.usian.controller;

import com.usian.fegin.SearchFegin;
import com.usian.utils.JsonUtils;
import com.usian.vo.ItemESVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页搜索的接口
 */
@RestController
@RequestMapping("/frontend/searchItem")
public class SearchController {

    @Autowired
    private SearchFegin searchFegin;

    @RequestMapping("/list")
    public String search(String q){
       List<ItemESVo> esVos = searchFegin.search(q);

        return JsonUtils.objectToJson(esVos);
    }
}
