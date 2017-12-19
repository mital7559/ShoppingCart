package com.yash.shop.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yash.shop.dao.CartDAO;
import com.yash.shop.dto.CartDTO;
import com.yash.shop.dto.CartItemDTO;
import com.yash.shop.dto.ItemDTO;
import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

@Service
public class CartServiceImpl implements CartService{

	@Autowired
	private CartDAO cartDAO;
	
	@Override
	public CartDTO getCartById(long cartId) throws CartNotFoundException{
		
		System.out.println("cart service called: "  + cartId);
		Cart cart =  cartDAO.getCartById(cartId);
		if(cart == null)
		{
			throw new CartNotFoundException("Cart Not Found Exception");
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
		List<Cart> cartList = cartDAO.getAllCarts();
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
	
//	@Override
//	public Set<CartItemDTO> getAllCartsWithItemsTest() {
//		List<CartItemDTO> cartList = cartDAO.getAllCartsWithItemsTest();
//		System.out.println("--Cart List--"+cartList.toString());
//		Set<CartItemDTO> dtoList  = new HashSet<CartItemDTO>();
//		
//		CartItemDTO cartItemDTOTemp = null;
//		for(CartItemDTO cartItemDTO : cartList)
//		{
//			cartItemDTOTemp = new CartItemDTO();
//			cartItemDTOTemp.setCart_id(cartItemDTO.getCart_id());
//			cartItemDTOTemp.setName(cartItemDTO.getName());
//			cartItemDTOTemp.setCart_createdAt(cartItemDTO.getCart_createdAt());
//			cartItemDTOTemp.setCart_updatedAt(cartItemDTO.getCart_updatedAt());
//			
//			Set<ItemDTO> itemSet = cartItemDTOTemp.getItems();
//			Set<ItemDTO> itemList = new HashSet<ItemDTO>();
//			
//			for (ItemDTO item : itemSet) {
//				ItemDTO itemDTO = new ItemDTO();
//			    itemDTO.setItem_id(item.getItem_id());
//			    itemDTO.setDescription(item.getDescription());
//			    itemDTO.setCreatedAt(item.getCreatedAt());
//			    itemDTO.setUpdatedAt(item.getUpdatedAt());
//			    itemDTO.setCart_id(item.getCart_id());
//			    itemList.add(itemDTO);
//			}
//			cartItemDTOTemp.setItems(itemList);
//			dtoList.add(cartItemDTOTemp);
//		}
//		return dtoList;
//	}
	
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
		System.out.println("Current time:---"+new Timestamp(System.currentTimeMillis()).toString());
		cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toString());
		long cartId = cartDAO.addCart(cart);
		
		return cartId;
	}

	@Override
	public CartDTO updateCart(CartDTO cartDTO) {
		Cart cart = new Cart();
		cart.setId(cartDTO.getId());
		cart.setName(cartDTO.getName());
		cart.setUpdatedAt(new Timestamp(System.currentTimeMillis()).toString());
		cart = cartDAO.updateCart(cart);
		
		return cartDTO;
		
	}

	@Override
	public void removeCart(long cartId) {
		cartDAO.removeCart(cartId);
		
	}

	@Override
	public long addItemInCart(ItemDTO itemDTO) {
		Item item = new Item();
		item.setDescription(itemDTO.getDescription());
		item.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
		//item.setCart(itemDTO.getCart());
		item.setCart_id(itemDTO.getCart_id());
		long itemId = cartDAO.addItemInCart(item);
		
		return itemId;
	}

	@Override
	public ItemDTO getAnItemFromCart(long cartId, long itemId) {
		Item item = cartDAO.getAnItemFromCart(cartId, itemId);
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
		cartDAO.removeItem(cartId, itemId);
		
	}

	


}
