package com.demo.zxl.user.mt.moudle.bean;

import java.util.List;

/**
 * Created by HASEE on 2016/12/13.
 */
public class HomeInfo {
    private Head head;
    private List<HomeItem> body;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<HomeItem> getBody() {
        return body;
    }

    public void setBody(List<HomeItem> body) {
        this.body = body;
    }
}
