package com.wteam.car.bean.interact.request;


public class PageInfo {


    public PageInfo() {
        pageSize = 10;
        currPage = 1;
    }

    private Integer pageSize;

    private Integer currPage;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrPage() {
        return currPage;
    }

    public void setCurrPage(Integer currPage) {
        this.currPage = currPage;
    }
}
