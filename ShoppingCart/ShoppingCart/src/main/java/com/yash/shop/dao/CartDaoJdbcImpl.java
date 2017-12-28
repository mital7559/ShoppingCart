package com.yash.shop.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

public class CartDaoJdbcImpl {

	
	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}  
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Cart getCartById(long cartId) throws CartNotFoundException{
		   
		 String sql = "SELECT * FROM cart_details WHERE id = ?";
		 
		 Cart cart = (Cart) jdbcTemplate.queryForObject(
		 sql, new Object[] { cartId }, new BeanPropertyRowMapper(Cart.class));
		 
		 if(cart == null)
		 {
			 throw new CartNotFoundException("Cart Not Found for ID : "+cartId);
		 }
		 
		 return cart;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Cart> getAllCarts() {
		
		System.out.println("IN JDBC Template--");
		
		String sql = "SELECT * FROM cart_details ";
		
		List<Cart> cartList = (List<Cart>) jdbcTemplate.query(sql, new BeanPropertyRowMapper(Cart.class));
		
		return cartList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public long addCart(Cart cart) {
		
		String sql = "insert into cart_details(name,updatedAt) values(?,?) ";
		
		jdbcTemplate.update(sql,cart.getName(),cart.getUpdatedAt());
		
		sql = "SELECT * FROM cart_details Order by id desc limit 1";
		
		Cart cartResult = (Cart) jdbcTemplate.queryForObject(
				 sql, new BeanPropertyRowMapper(Cart.class));
		
		return cartResult.getId();
		
	}
	
	public Cart updateCart(Cart cart) throws CartNotFoundException{
		String sql = "update cart_details set name=? where id=? ";
		jdbcTemplate.update(sql,cart.getName(),cart.getId());
		return cart;
	}
	
	
	public void removeCart(long cartId) throws CartNotFoundException{
		String sql = "delete from cart_details where id=? ";
		jdbcTemplate.update(sql,cartId);
		
		sql = "delete from item_details where cart_id = ?";
		jdbcTemplate.update(sql,cartId);
	}
	
	public long addItemInCart(Item item) {
		String sql = "insert into item_details(description,updatedAt,cart_id) values(?,?,?) ";
		
		return jdbcTemplate.update(sql,item.getDescription(),item.getUpdatedAt(),item.getCart_id());
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Item getAnItemFromCart(long cartId, long itemId) {
		String sql = "select * from item_details i where i.item_id = ? and i.cart_id = ? ";
		
		Item item = (Item) jdbcTemplate.queryForObject(
				 sql, new Object[] { itemId,cartId }, new BeanPropertyRowMapper(Item.class));
		
		return item;
	}
	
	public void removeItem(long cartId,long itemId) {
		
		String sql = "delete from item_details where cart_id = ? and item_id=? ";
		jdbcTemplate.update(sql,cartId,itemId);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Item> getItemsFromCart(long cartId) {
		
		String sql = "SELECT * FROM item_details where cart_id=? ";
		
		List<Item> itemList = (List<Item>) jdbcTemplate.query(sql,new Object[] { cartId } ,new BeanPropertyRowMapper(Item.class));
		return itemList;
	}
}
