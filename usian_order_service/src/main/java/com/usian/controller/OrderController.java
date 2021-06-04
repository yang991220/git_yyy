package com.usian.controller;

import com.usian.service.OrderService;
import com.usian.vo.ItemCartTermVo;
import com.usian.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/selectOrderTerms")
    public List<ItemCartTermVo> selectOrderTerms(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId ){
        return orderService.selectOrderTerms(ids,userId);
    }

    @RequestMapping("/insertOrder")
    String insertOrder(@RequestBody OrderVo orderVo){
       return orderService.insertOrder(orderVo);
    }
}
