package com.usian.api;

import com.usian.vo.ItemCartTermVo;
import com.usian.vo.OrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-order-service")
public interface OrderFeign {

    @RequestMapping("/order/selectOrderTerms")
    public List<ItemCartTermVo> selectOrderTerms(@RequestParam("ids") Long[] ids, @RequestParam("userId") Long userId );

    @RequestMapping("/order/insertOrder")
    String insertOrder(@RequestBody OrderVo orderVo);
}
