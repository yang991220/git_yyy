package com.usian.dto;

import com.usian.pojo.TbItem;
import com.usian.pojo.TbItemDesc;
import com.usian.pojo.TbItemParam;

import java.io.Serializable;

public class ItemDto implements Serializable {
    private TbItem tbItem;
    private TbItemDesc tbItemDesc;
    private TbItemParam tbItemParam;
    private String categoryName;

    public TbItemParam getTbItemParam() {
        return tbItemParam;
    }

    public void setTbItemParam(TbItemParam tbItemParam) {
        this.tbItemParam = tbItemParam;
    }

    public TbItem getTbItem() {
        return tbItem;
    }

    public void setTbItem(TbItem tbItem) {
        this.tbItem = tbItem;
    }

    public TbItemDesc getTbItemDesc() {
        return tbItemDesc;
    }

    public void setTbItemDesc(TbItemDesc tbItemDesc) {
        this.tbItemDesc = tbItemDesc;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
