package com.yash.shop.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.yash.shop.model.Item;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id;
	
	private String name;
	
	private String createdAt;
	
	private String updatedAt;
	
	private Set<ItemDTO> items = new HashSet<ItemDTO>(0);
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Set<ItemDTO> getItems() {
		return items;
	}

	public void setItems(Set<ItemDTO> items) {
		this.items = items;
	}


	
}
