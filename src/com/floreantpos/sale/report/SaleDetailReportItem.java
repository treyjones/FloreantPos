package com.floreantpos.sale.report;

import java.util.List;

import com.floreantpos.model.TaxTreatment;

public class SaleDetailReportItem {
	private String date;
	private String id;
	private String name;
	private double sellPrice;
	private double buyPrice;
	private double profit;
	private int quantity;
	private List<TaxTreatment> taxList;
	private double discount;

	public SaleDetailReportItem() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return sellPrice;
	}

	public void setPrice(double price) {
		this.sellPrice = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public double getProfit() {
		return profit;
	}

	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public List<TaxTreatment> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxTreatment> taxList) {
		this.taxList = taxList;
	}

}
