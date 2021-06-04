package com.usian.controller;

import com.usian.service.CartService;
import com.usian.vo.ItemCartTermVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("/addItem")
    public void addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId){
        cartService.addItem(userId,itemId);
    }


    @RequestMapping("/showCart")
    public List<ItemCartTermVo> getCart(@RequestParam("userId") Long userId){
        return cartService.getCart(userId);
    }

    @RequestMapping("/updateItemNum")
    public void updateItemNum(@RequestParam("num") Integer num,@RequestParam("userId") Long userId,@RequestParam("itemId") Long itemId){
        cartService.updateItemnums(num,userId,itemId);
    }

    @RequestMapping("/deleteItemFromCart")
    public void deleteItemFromCarts(@RequestParam("userId") Long userId,@RequestParam("itemId") Long itemId){
        cartService.deleteItemFromCarts(userId,itemId);
    }
}
