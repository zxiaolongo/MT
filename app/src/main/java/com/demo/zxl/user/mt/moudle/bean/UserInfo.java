package com.demo.zxl.user.mt.moudle.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by HASEE on 2016/12/16.
 */
@DatabaseTable(tableName = "t_user")
public class UserInfo {
    @DatabaseField(id = true)
    private int _id;

    @DatabaseField()
    private float balance;

    @DatabaseField()
    private float discount;

    @DatabaseField()
    private int integral;

    @DatabaseField()
    private String name;

    @DatabaseField()
    private String phone;

    @DatabaseField()
    private int isLogin; //1 代表已经登陆     0 代表没有登陆

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getLogin() {
        return isLogin;
    }

    public void setLogin(int isLogin) {
        this.isLogin = isLogin;
    }
}
