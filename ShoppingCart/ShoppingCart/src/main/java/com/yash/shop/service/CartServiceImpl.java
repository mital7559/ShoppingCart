package com.yash.shop.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yash.shop.dao.CartDAO;
import com.yash.shop.dao.CartDaoJdbcImpl;
import com.yash.shop.dto.CartDTO;
import com.yash.shop.dto.CartItemDTO;
import com.yash.shop.dto.ItemDTO;
import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

@Service
public class CartServiceImpl implements CartService{

	
	private CartDAO cartDAO;
	
	private CartDaoJdbcImpl cartDaoJdbc;
	
	private String connection;
	
	@Autowired
	public void setCartDAO(CartDAO cartDAO) {
		this.cartDAO = cartDAO;
	}
	
	@Autowired
	public void setCartDaoJdbc(CartDaoJdbcImpl cartDaoJdbc) {
		this.cartDaoJdbc = cartDaoJdbc;
	}
	
	public void setConnection(String connection) {
		this.connection = connection;
	}
	
	public String getConnection() {
		return connection;
	}

	public CartServiceImpl(CartDAO cartDAO,CartDaoJdbcImpl cartDaoJdbcImpl) {
		this.cartDAO = cartDAO;
		this.cartDaoJdbc = cartDaoJdbcImpl;
	}
	
	public CartServiceImpl() {
		
	}

	@Override
	public CartDTO getCartById(long cartId) throws CartNotFoundException{
		
		System.out.println("cart service called: "  + cartId);
		Cart cart;
		
		if(getConnection().equalsIgnoreCase("hibernate"))
		{
			cart =  cartDAO.getCartById(cartId);
		}
		else
		{
			System.out.println("IN JDBC Service");
			cart = cartDaoJdbc.getCartById(cartId);
		}
		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(cart.getId());
		cartDTO.setName(cart.getName());
		cartDTO.setCreatedAt(cart.getCreatedAt());
		cartDTO.setUpdatedAt(cart.getUpdatedAt());
		cartDTO.setItems(null);
		return cartDTO;
		
	}

	@Override
	public List<CartDTO> getAllCarts() {
		
		List<Cart> cartList;
		
		if(getConnection().equalsIgnoreCase("hibernate")){
			cartList = cartDAO.getAllCarts();
		}else{
			cartList = cartDaoJdbc.getAllCarts();
		}
		
		List<CartDTO> dtoList  = new ArrayList<CartDTO>();
		
		for(Cart cart : cartList)
		{
			CartDTO cartDTO = new CartDTO();
			cartDTO.setId(cart.getId());
			cartDTO.setName(cart.getName());
			cartDTO.setCreatedAt(cart.getCreatedAt());
			cartDTO.setUpdatedAt(cart.getUpdatedAt());
			cartDTO.setItems(null);
			dtoList.add(cartDTO);
		}
		return dtoList;
	}
	
	
	@Override
	public List<CartDTO> getAllCartsWithItems() {
		List<Cart> cartList = cartDAO.getAllCartsWithItems();
		System.out.println("--Cart List--"+cartList.toString());
		List<CartDTO> dtoList  = new ArrayList<CartDTO>();
		CartDTO cartDTO = null;
		
		Set<Integer> tempCardId = new HashSet<Integer>();
		for(Cart cart : cartList)
		{

			
			cartDTO = new CartDTO();
			cartDTO.setId(cart.getId());
			cartDTO.setName(cart.getName());
			cartDTO.setCreatedAt(cart.getCreatedAt());
			cartDTO.setUpdatedAt(cart.getUpdatedAt());
			
			Set<Item> itemSet = cart.getItems();
			Set<ItemDTO> itemList = new HashSet<ItemDTO>();
			
			for (Item item : itemSet) {
				ItemDTO itemDTO = new ItemDTO();
			    itemDTO.setItem_id(item.getItem_id());
			    itemDTO.setDescription(item.getDescription());
			    itemDTO.setCreatedAt(item.getCreatedAt());
			    itemDTO.setUpdatedAt(item.getUpdatedAt());
			    itemDTO.setCart_id(item.getCart_id());
			    itemList.add(itemDTO);
			}
			cartDTO.setItems(itemList);
			
			if(tempCardId.add((int) cart.getId()))
			{
				dtoList.add(cartDTO);
			}
		}
		return dtoList;
	}

	@Override
	public long addCart(CartDTO cartDTO) {
		
		Cart cart = new Cart();
		cart.setName(cartDTO.getName());
		cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toString());
		
		long cartId;
		if(getConnection().equalsIgnoreCase("hibernate")){
		
			cartId = cartDAO.addCart(cart);
		}
		else{
			cartId = cartDaoJdbc.addCart(cart);
		}
		return cartId;
	}

	@Override
	public CartDTO updateCart(CartDTO cartDTO) throws CartNotFoundException{
		Cart cart = new Cart();
		cart.setId(cartDTO.getId());
		cart.setName(cartDTO.getName());
		cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toString());
		
		if(getConnection().equalsIgnoreCase("hibernate")){
			cart = cartDAO.updateCart(cart);
		}
		else{
			cart = cartDaoJdbc.updateCart(cart);
		}
		
		return cartDTO;
		
	}

	@Override
	public void removeCart(long cartId) throws CartNotFoundException{
		if(getConnection().equalsIgnoreCase("hibernate")){
			cartDAO.removeCart(cartId);
		}
		else{
			cartDaoJdbc.removeCart(cartId);
		}
	}

	@Override
	public long addItemInCart(ItemDTO itemDTO) {
		Item item = new Item();
		item.setDescription(itemDTO.getDescription());
		item.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
		item.setCart_id(itemDTO.getCart_id());
		
		long itemId;
		if(getConnection().equalsIgnoreCase("hibernate")){
			itemId = cartDAO.addItemInCart(item);
		}
		else{
			itemId = cartDaoJdbc.addItemInCart(item);
		}
		return itemId;
	}

	@Override
	public ItemDTO getAnItemFromCart(long cartId, long itemId) {
		Item item;
		if(getConnection().equalsIgnoreCase("hibernate")){
			item = cartDAO.getAnItemFromCart(cartId, itemId);
		}
		else{
			item = cartDaoJdbc.getAnItemFromCart(cartId, itemId);
		}
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setItem_id(item.getItem_id());
		itemDTO.setDescription(item.getDescription());
		itemDTO.setCreatedAt(item.getCreatedAt());
		itemDTO.setUpdatedAt(item.getUpdatedAt());
		itemDTO.setCart_id(item.getCart_id());
		
		return itemDTO;
	}

	@Override
	public void removeItem(long cartId, long itemId) {
		if(getConnection().equalsIgnoreCase("hibernate")){
			cartDAO.removeItem(cartId, itemId);
		}
		else{
			cartDaoJdbc.removeItem(cartId,itemId);
		}
		
	}

	@Override
	public List<ItemDTO> getItemsFromCart(long cartId) {
		
		List<Item> itemList = new ArrayList<Item>();
		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
		
		if(getConnection().equalsIgnoreCase("hibernate")){
			itemList = cartDAO.getItemsFromCart(cartId);
		}else{
			itemList = cartDaoJdbc.getItemsFromCart(cartId);
		}
		
		for(Item item : itemList)
		{
			ItemDTO itemDTO = new ItemDTO();
			itemDTO.setItem_id(item.getItem_id());
			itemDTO.setCart_id(item.getCart_id());
			itemDTO.setDescription(item.getDescription());
			itemDTO.setCreatedAt(item.getCreatedAt());
			itemDTO.setUpdatedAt(item.getUpdatedAt());
			
			itemDTOList.add(itemDTO);
		}
		
		return itemDTOList;
	}

	


}
