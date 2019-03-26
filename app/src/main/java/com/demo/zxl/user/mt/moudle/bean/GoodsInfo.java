package com.demo.zxl.user.mt.moudle.bean;

import java.io.Serializable;

/**
 * Created by HASEE on 2016/12/15.
 */

public class GoodsInfo implements Serializable {
    private int id;
    private boolean bargainPrice;
    private String form;
    private String icon;
    private int monthSaleNum;
    private String name;
    private boolean isNew;//服务器字段传错了,用了关键字
    private float newPrice;
    private float oldPrice;

    private int count;//商品选择的数量
    private int typeId;//用于指定此商品所属的商品类型id
    private int sellerId;//商品所在商铺的id
    private String typeName;//类型名称

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public boolean isBargainPrice() {
        return bargainPrice;
    }
    public void setBargainPrice(boolean bargainPrice) {
        this.bargainPrice = bargainPrice;
    }
    public String getForm() {
        return form;
    }
    public void setForm(String form) {
        this.form = form;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public int getMonthSaleNum() {
        return monthSaleNum;
    }
    public void setMonthSaleNum(int monthSaleNum) {
        this.monthSaleNum = monthSaleNum;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isNew() {
        return isNew;
    }
    public void setNew(boolean aNew) {
        isNew = aNew;
    }
    public float getNewPrice() {
        return newPrice;
    }
    public void setNewPrice(float newPrice) {
        this.newPrice = newPrice;
    }
    public float getOldPrice() {
        return oldPrice;
    }
    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
