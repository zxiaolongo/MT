package com.demo.zxl.user.mt.moudle.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Seller implements Serializable {
	
	/**
	 * "id":1,
                    "pic":http://xxxxxxxxxx.jpg,
                    "name":"二十五块半（上地店）",
                    
                    "score":"4.4",
                    "sale":4132,//销量
                    "ensure":1,//是否有转送保证
                    
                    "invoice":1,//是否提供发票
                    "sendPrice":20,//起送价格
                    "deliveryFee":4,//配送费
                    
                    "recentVisit":1,//是否最近光顾
                    "distance":"773m",
                    "time":"34分钟",
                    
                    "activityList":,[{//活动列表
                       "id":1,
                       "type":1,// 活动类型，详见附表
                       "info":"在线支付，满30减8"
},{
   "id":2,
   "type":2,
   " info ":"16.9元特价超值套餐！"
}]

	 */
	
	private long id;//商家唯一性id
	private String pic;//商家头像
	private String name;
	
	private String score;
	private String sale;
	private String ensure;
	
	private String invoice;
	private String sendPrice;//起送价
	private String deliveryFee;//配送费
	
	private String recentVisit;
	private String distance;
	private String time;
	
	private ArrayList<ActivityInfo> activityList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public String getEnsure() {
		return ensure;
	}

	public void setEnsure(String ensure) {
		this.ensure = ensure;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public String getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(String sendPrice) {
		this.sendPrice = sendPrice;
	}

	public String getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(String deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public String getRecentVisit() {
		return recentVisit;
	}

	public void setRecentVisit(String recentVisit) {
		this.recentVisit = recentVisit;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public ArrayList<ActivityInfo> getActivityList() {
		return activityList;
	}

	public void setActivityList(ArrayList<ActivityInfo> activityList) {
		this.activityList = activityList;
	}
	

	
	
	
	

}
