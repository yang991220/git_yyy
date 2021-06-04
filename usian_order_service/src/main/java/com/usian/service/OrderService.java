package com.usian.service;

import com.usian.vo.ItemCartTermVo;
import com.usian.vo.OrderVo;

import java.util.List;

public interface OrderService {
    List<ItemCartTermVo> selectOrderTerms(Long[] ids, Long userId);

    String insertOrder(OrderVo orderVo);
}
