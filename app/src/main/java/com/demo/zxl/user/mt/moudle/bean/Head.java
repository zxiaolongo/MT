package com.demo.zxl.user.mt.moudle.bean;

import java.util.ArrayList;

public class Head {
	ArrayList<Promotion> promotionList;
	ArrayList<Category> categorieList;

	public Head(ArrayList<Promotion> promotionList, ArrayList<Category> categorieList) {
		super();
		this.promotionList = promotionList;
		this.categorieList = categorieList;

	}

	public ArrayList<Promotion> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(ArrayList<Promotion> promotionList) {
		this.promotionList = promotionList;
	}

	public ArrayList<Category> getCategorieList() {
		return categorieList;
	}

	public void setCategorieList(ArrayList<Category> categorieList) {
		this.categorieList = categorieList;
	}

}
