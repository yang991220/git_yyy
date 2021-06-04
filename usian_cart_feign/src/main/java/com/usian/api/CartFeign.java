package com.usian.api;

import com.usian.vo.ItemCartTermVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-cart-service")
public interface CartFeign {


    @RequestMapping("/cart/deleteItemFromCart")
    public void deleteItemFromCarts(@RequestParam("userId") Long userId,@RequestParam("itemId") Long itemId);

    @RequestMapping("/cart/updateItemNum")
    public ItemCartTermVo updateItemnums(@RequestParam("num") Integer num,@RequestParam("userId") Long userId,@RequestParam("itemId") Long itemId);

    @RequestMapping("/cart/showCart")
    public List<ItemCartTermVo> getCart(@RequestParam("userId") Long userId);



    @RequestMapping("/cart/addItem")
    public void addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId);
}
