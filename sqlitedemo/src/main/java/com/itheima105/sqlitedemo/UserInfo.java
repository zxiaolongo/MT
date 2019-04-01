package com.itheima105.sqlitedemo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by HASEE.
 */
@DatabaseTable(tableName = "t_user")
public class UserInfo {
    @DatabaseField(generatedId = true)//_id的值数据库中自增长主键
    private int _id;

    @DatabaseField()
    private String username;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
