package com.usian.controller;

import com.usian.api.OrderFeign;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import com.usian.utils.Result;
import com.usian.vo.ItemCartTermVo;
import com.usian.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/order")
public class OrderController {

    @Autowired
    private OrderFeign orderFeign;



    //提交订单接口
    @RequestMapping("/insertOrder")
    public Result insertOrder(String orderItem, TbOrderShipping orderShipping, TbOrder order){
       String orderId = orderFeign.insertOrder(new OrderVo(orderItem,order,orderShipping));
        return Result.ok(orderId);
    }

    //去结算接口
    @RequestMapping("/goSettlement")
    public Result goSettlement(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId, @RequestParam("token") String token){
        List<ItemCartTermVo> datas = orderFeign.selectOrderTerms(ids,userId);

        return Result.ok(datas);

    }
}
