package com.yash.shop.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yash.shop.dto.CartDTO;
import com.yash.shop.dto.CartItemDTO;
import com.yash.shop.dto.ItemDTO;
import com.yash.shop.dto.StatusDTO;
import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.service.CartService;

@RestController
@RequestMapping("/rest")
public class CartController {

	@Autowired
	private CartService cartService;
	
	public CartController(CartService cartService){
		this.cartService = cartService;
	}
	
	public CartController(){
		
	}
	
	
	/**
	 * To Get All Carts details
	 * 
	 * @return
	 */
	@GetMapping(value="/shopping_carts",produces={MediaType.APPLICATION_JSON_VALUE})
	public List<CartDTO> getAllCarts(){
		
		return cartService.getAllCarts();
		
	}
	
	/**
	 * To Get All Carts details
	 * 
	 * @return
	 */
	@GetMapping(value="/shopping_carts/include_items",produces={MediaType.APPLICATION_JSON_VALUE})
	public List<CartDTO> getAllCartsWithItems(){
		
		return cartService.getAllCartsWithItems();
	}
	
	
	/**
	 * Add new cart
	 * 
	 * @param cartDTO
	 * @return
	 */
	@PostMapping(value="/shopping_carts",consumes={MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_JSON_VALUE})
	public CartDTO addCart(@RequestBody CartDTO cartDTO) throws CartNotFoundException{
		
		System.out.println("calling add cart method"+cartDTO.getName());
		long cartId = cartService.addCart(cartDTO);
		
		return cartService.getCartById(cartId);
	}
	
	/**
	 * Update cart details
	 * 
	 * @param cartDTO
	 * @param cartId
	 * @return
	 */
	@PutMapping(value="/shopping_carts/{cartId}",consumes={MediaType.APPLICATION_JSON_VALUE},produces={MediaType.APPLICATION_JSON_VALUE})
	public  CartDTO updateCart(@RequestBody CartDTO cartDTO,@PathVariable("cartId") long cartId) throws CartNotFoundException{
		cartDTO.setId(cartId);
		if(cartId>0){
			cartDTO = cartService.updateCart(cartDTO);
		}
		return cartService.getCartById(cartId);
	}
	
	/**
	 *  To retrive Cart details by ID
	 * 
	 * @param cartId
	 * @return
	 */
	@GetMapping(value="/shopping_carts/{cartId}",produces={MediaType.APPLICATION_JSON_VALUE})
	public CartDTO getShoppingCart(@PathVariable("cartId") long cartId) throws CartNotFoundException{
		System.out.println("calling get Cart Details method   "+cartId);
		
		return cartService.getCartById(cartId);
		
	}
	
	@DeleteMapping(value="/shopping_carts/{cartId}",produces={MediaType.APPLICATION_JSON_VALUE})
	public StatusDTO deleteCart(@PathVariable("cartId") long cartId){
		
		cartService.removeCart(cartId);
		StatusDTO status = new StatusDTO();
		status.setMessage("Cart Deleted Successfully");
		status.setStatus(200);
		return status;
		
	}
	
	/*******************  Items APIs  ********************/
	
	@PostMapping(value="/shopping_carts/{cartId}/items",consumes={MediaType.APPLICATION_JSON_VALUE}, produces={MediaType.APPLICATION_JSON_VALUE})
	public StatusDTO addItemInCart(@RequestBody ItemDTO itemDTO,@PathVariable("cartId") long cartId){
		
		Cart cart = new Cart();
		cart.setId(cartId);

		//itemDTO.setCart(cart);
		itemDTO.setCart_id(cartId);
		long itemId = cartService.addItemInCart(itemDTO);
		
		StatusDTO status = new StatusDTO();
		status.setMessage("Item Added Successfully");
		status.setStatus(200);
		return status;
	}
	
	
	@GetMapping(value="/shopping_carts/{cartId}/items/{itemId}", produces={MediaType.APPLICATION_JSON_VALUE})
	public ItemDTO getItemFromCart(@PathVariable("cartId") long cartId, @PathVariable("itemId") long itemId){
		
		ItemDTO itemDTO = cartService.getAnItemFromCart(cartId, itemId);
		return itemDTO;
	}
	
	@DeleteMapping(value="/shopping_carts/{cartId}/items/{itemId}",produces={MediaType.APPLICATION_JSON_VALUE})
	public StatusDTO deleteItem(@PathVariable("cartId") long cartId,@PathVariable("itemId") long itemId){
		
		cartService.removeItem(cartId,itemId);
		StatusDTO status = new StatusDTO();
		status.setMessage("Item Deleted Successfully");
		status.setStatus(200);
		return status;
		
	}
}
