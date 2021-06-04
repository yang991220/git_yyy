package com.usian.controller;

import com.usian.service.SearchService;
import com.usian.vo.ItemESVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class ESController {

    @Autowired
    private SearchService searchService;


    @RequestMapping("/importAll")
    public void test(){
        searchService.importAll();
    }




    @RequestMapping("/list")
    public List<ItemESVo> search(@RequestParam("q") String q){
       return searchService.search(q);
    }

}
