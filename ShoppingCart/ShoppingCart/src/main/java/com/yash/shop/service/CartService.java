package com.yash.shop.service;

import java.util.List;
import java.util.Set;

import com.yash.shop.dto.CartDTO;
import com.yash.shop.dto.CartItemDTO;
import com.yash.shop.dto.ItemDTO;
import com.yash.shop.exception.CartNotFoundException;

public interface CartService {
	
	public long addCart(CartDTO cartDTO);
	public List<CartDTO> getAllCarts();
	public CartDTO getCartById(long cartId) throws CartNotFoundException;
	public CartDTO updateCart(CartDTO cartDTO);
	public void removeCart(long cartId);
	
	public List<CartDTO> getAllCartsWithItems();
	
//	public Set<CartItemDTO> getAllCartsWithItemsTest();
	
	
	public long addItemInCart(ItemDTO itemDTO);
	public ItemDTO getAnItemFromCart(long cartId,long itemId);
	public void removeItem(long cartId,long itemId);
}
