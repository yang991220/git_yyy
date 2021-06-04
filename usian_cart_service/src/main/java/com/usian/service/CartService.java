package com.usian.service;

import com.usian.vo.ItemCartTermVo;

import java.util.List;

public interface CartService {
    void addItem(Long userId, Long itemId);

    List<ItemCartTermVo> getCart(Long userId);

    ItemCartTermVo updateItemnums(Integer num, Long userId, Long itemId);

    void deleteItemFromCarts(Long userId, Long itemId);
}
