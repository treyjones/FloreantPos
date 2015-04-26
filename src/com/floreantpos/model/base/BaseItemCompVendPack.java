package com.floreantpos.model.base;

import com.floreantpos.model.Company;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryVendor;
import com.floreantpos.model.ItemCompVendPack;
import com.floreantpos.model.PackSize;

// Generated 25 Apr, 2015 1:56:53 PM by Hibernate Tools 3.4.0.CR1

/**
 * ItemCompVendPack generated by hbm2java
 */
public class BaseItemCompVendPack implements java.io.Serializable {
	public static String PROP_ID = "id";
	public static String PROP_PACK_SIZE = "packSize";
	public static String PROP_COMPANY = "company";
	public static String PROP_INV_VENDOR = "inventoryVendor";
	public static String PROP_INV_ITEM = "inventoryItem";

	protected Integer id;
	protected PackSize packSize;
	protected Company company;
	protected InventoryVendor inventoryVendor;
	protected InventoryItem inventoryItem;
	private int hashCode = Integer.MIN_VALUE;

	public BaseItemCompVendPack() {
	}

	public BaseItemCompVendPack(PackSize packSize, Company company, InventoryVendor inventoryVendor, InventoryItem inventoryItem) {
		this.packSize = packSize;
		this.company = company;
		this.inventoryVendor = inventoryVendor;
		this.inventoryItem = inventoryItem;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PackSize getPackSize() {
		return this.packSize;
	}

	public void setPackSize(PackSize packSize) {
		this.packSize = packSize;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public InventoryVendor getInventoryVendor() {
		return this.inventoryVendor;
	}

	public void setInventoryVendor(InventoryVendor inventoryVendor) {
		this.inventoryVendor = inventoryVendor;
	}

	public InventoryItem getInventoryItem() {
		return this.inventoryItem;
	}

	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof ItemCompVendPack))
			return false;
		else {
			ItemCompVendPack itemCompVendPack = (ItemCompVendPack) obj;
			return (this.getCompany().equals(itemCompVendPack.getCompany()) && this.getInventoryVendor().equals(itemCompVendPack.getInventoryVendor()) && this.getPackSize().equals(
					itemCompVendPack.getPackSize()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

}
