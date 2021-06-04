package com.usian.vo;

import java.io.Serializable;

/**
 * 购物车项
 */
public class ItemCartTermVo implements Serializable {
    private Long id; //商品id
    private String title; //商品标题
    private String image;//图片
    private Integer num; //购买的数量
    private Long price; //单价
    private String sellPoint; //卖点

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint;
    }
}
