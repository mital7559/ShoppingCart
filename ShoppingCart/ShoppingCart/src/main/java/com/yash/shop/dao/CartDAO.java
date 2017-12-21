package com.yash.shop.dao;

import java.util.List;

import com.yash.shop.dto.CartItemDTO;
import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

public interface CartDAO {

	public long addCart(Cart cart);
	public List<Cart> getAllCarts();
	public Cart getCartById(long cartId) throws CartNotFoundException;
	public Cart updateCart(Cart cart) throws CartNotFoundException;
	public void removeCart(long cartId) throws CartNotFoundException;
	
	public List<Cart> getAllCartsWithItems();
	
//	public List<CartItemDTO> getAllCartsWithItemsTest();
	
	public long addItemInCart(Item item);
	public Item getAnItemFromCart(long cartId, long itemId);
	public void removeItem(long cartId, long itemId);
	public List<Item> getItemsFromCart(long cartId);
}
