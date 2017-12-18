package com.yash.shop.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CartItemDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long cart_id;
	
	private String name;
	
	private String cart_createdAt;
	
	public long getCart_id() {
		return cart_id;
	}

	public void setCart_id(long cart_id) {
		this.cart_id = cart_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCart_createdAt() {
		return cart_createdAt;
	}

	public void setCart_createdAt(String cart_createdAt) {
		this.cart_createdAt = cart_createdAt;
	}

	public String getCart_updatedAt() {
		return cart_updatedAt;
	}

	public void setCart_updatedAt(String cart_updatedAt) {
		this.cart_updatedAt = cart_updatedAt;
	}

	public Set<ItemDTO> getItems() {
		return items;
	}

	public void setItems(Set<ItemDTO> items) {
		this.items = items;
	}

	public long getItem_id() {
		return item_id;
	}

	public void setItem_id(long item_id) {
		this.item_id = item_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getItem_createdAt() {
		return item_createdAt;
	}

	public void setItem_createdAt(String item_createdAt) {
		this.item_createdAt = item_createdAt;
	}

	public String getItem_updatedAt() {
		return item_updatedAt;
	}

	public void setItem_updatedAt(String item_updatedAt) {
		this.item_updatedAt = item_updatedAt;
	}

	private String cart_updatedAt;
	
	private Set<ItemDTO> items = new HashSet<ItemDTO>(0);
	
	private long item_id;
	
	private String description;
	
	private String item_createdAt;
	
	private String item_updatedAt;
	
}
