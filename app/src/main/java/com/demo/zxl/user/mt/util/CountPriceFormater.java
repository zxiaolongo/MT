package com.demo.zxl.user.mt.util;

import java.text.NumberFormat;

/**
 * 格式化总价格的工具类  自带￥   保留两位小数
 */
public class CountPriceFormater {

    private CountPriceFormater(){

    }

    //格式化总价格的工具类  自带￥   保留两位小数
    public static String format(float countPrice){
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(2);
        return format.format(countPrice);
    }

}
