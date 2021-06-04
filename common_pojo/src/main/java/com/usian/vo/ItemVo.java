package com.usian.vo;

import com.usian.pojo.TbItem;

public class ItemVo extends TbItem {
    private String desc;
    private String itemParams;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemParams() {
        return itemParams;
    }

    public void setItemParams(String itemParams) {
        this.itemParams = itemParams;
    }
}
