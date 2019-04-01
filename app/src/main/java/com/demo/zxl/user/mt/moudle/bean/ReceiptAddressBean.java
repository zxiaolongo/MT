package com.demo.zxl.user.mt.moudle.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by HASEE on 2016/11/23.
 */
@DatabaseTable(tableName = "t_receiptaddress")
public class ReceiptAddressBean implements Serializable{
    @DatabaseField(generatedId = true)
    private int _id;
    @DatabaseField(columnName = "uid")
    private int uid;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "sex")
    private String sex;
    @DatabaseField(columnName = "phone")
    private String phone;
    @DatabaseField(columnName = "phoneOther")
    private String phoneOther;
    @DatabaseField(columnName = "receiptAddress")
    private String receiptAddress;
    @DatabaseField(columnName = "detailAddress")
    private String detailAddress;
    @DatabaseField(columnName = "label")
    private String label;
    @DatabaseField(columnName = "isSelect")
    private int isSelect;//1代表被选中,0代表没被选中

    public ReceiptAddressBean(int uid, String name, String sex, String phone, String phoneOther,
                              String receiptAddress, String detailAddress, String label,int isSelect) {
        this.uid = uid;
        this.name = name;
        this.sex = sex;
        this.phone = phone;
        this.phoneOther = phoneOther;
        this.receiptAddress = receiptAddress;
        this.detailAddress = detailAddress;
        this.label = label;
        this.isSelect = isSelect;
    }

    public ReceiptAddressBean() {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneOther() {
        return phoneOther;
    }

    public void setPhoneOther(String phoneOther) {
        this.phoneOther = phoneOther;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int isSelect() {
        return isSelect;
    }

    public void setSelect(int select) {
        isSelect = select;
    }
}
