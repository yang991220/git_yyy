package com.usian.utils;

public class PageResult {
    private Integer pageIndex;      //当前页
    private Integer totalPage;      //总页数
    private Object result;      //当前页的内容信息

    public PageResult() {
    }

    public PageResult(Integer pageIndex, Integer totalPage, Object result) {
        this.pageIndex = pageIndex;
        this.totalPage = totalPage;
        this.result = result;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
