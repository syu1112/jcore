package com.jfcore.web.vo;

public class PageVo {
    private int count;
    private int pcount;
    private int pno;
    private int psize = 20;
    private String orderby;

    public PageVo() {
        this.count = 0;
        this.pcount = 1;
        this.pno = 1;
        this.orderby = null;
    }

    public PageVo(int pno) {
        this();
        this.pno = pno;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
        this.pcount = (int) Math.ceil(1.0 * count / psize);
    }

    public int getPcount() {
        return pcount;
    }

    public void setPcount(int pcount) {
        this.pcount = pcount;
    }

    public int getPno() {
        return pno;
    }

    public void setPno(int pno) {
        this.pno = pno;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public String limitSql() {
        return " LIMIT " + (pno - 1) * psize + "," + psize;
    }

    public String orderbySql() {
        if (orderby != null) {
            return " ORDER BY " + this.orderby + " ";
        }
        return "";
    }

}
