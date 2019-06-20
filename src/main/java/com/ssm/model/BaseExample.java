package com.ssm.model;

public class BaseExample {
    public Integer pageStart;
    public Integer pageSize;
    
    public Integer getPageStart(){
        return pageStart;
    }
    public Integer getPageSize(){
        return pageSize;
    }
    public void setPageStart(Integer pageStart){
        this.pageStart = pageStart;
    }
    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }
}
