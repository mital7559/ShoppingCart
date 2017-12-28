package com.yash.shop.controller;

import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.shop.dto.CartDTO;
import com.yash.shop.dto.ItemDTO;
import com.yash.shop.dto.StatusDTO;
import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.service.CartService;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
    private CartService cartServiceMock;
	
	@InjectMocks
	private CartController cartController;
    
	@Before
    public void setUp() {
        
		mockMvc = MockMvcBuilders.standaloneSetup(new CartController(cartServiceMock))
                .setHandlerExceptionResolvers(exceptionResolver())
                .build();
    }
	
	 private HandlerExceptionResolver exceptionResolver() 
	 {
	        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

	        Properties exceptionMappings = new Properties();

	        exceptionMappings.put("java.lang.Exception", "error/error");
	        exceptionMappings.put("java.lang.RuntimeException", "error/error");

	        exceptionResolver.setExceptionMappings(exceptionMappings);

	        Properties statusCodes = new Properties();

	        statusCodes.put("error/404", "404");
	        statusCodes.put("error/error", "500");

	        exceptionResolver.setStatusCodes(statusCodes);

	        return exceptionResolver;
	    }
	 
	
    /**
     *  this test case is to check return cart list
     * @throws Exception
     */
    @Test
    public void getAllCarts_CartsFound_ShouldReturnFoundCartsEntries() throws Exception {
        CartDTO firstCart = new CartDTO();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
        
        CartDTO secondCart = new CartDTO();
        secondCart.setId(2);
        secondCart.setName("cart 2");
        secondCart.setCreatedAt("2017-12-16 12:35:08");
        secondCart.setUpdatedAt("2017-12-16 12:35:08");
        
        List<CartDTO> cartList = new ArrayList<CartDTO>();
        cartList.add(firstCart);
        cartList.add(secondCart);
        
        when(cartServiceMock.getAllCarts()).thenReturn(cartList);
 
        mockMvc.perform(get("/rest/shopping_carts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("cart 1")))
                .andExpect(jsonPath("$[0].createdAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$[0].updatedAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("cart 2")))
                .andExpect(jsonPath("$[1].createdAt", is("2017-12-16 12:35:08")))
                .andExpect(jsonPath("$[1].updatedAt", is("2017-12-16 12:35:08")));
 
        verify(cartServiceMock, times(1)).getAllCarts();
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    
    /**
     *  this test case is to check return cart list with all items
     * @throws Exception
     */
    @Test
    public void getAllCartsWithItems_CartsAndItemsFound_ShouldReturnFoundCartsAndItemsEntries() throws Exception {
        CartDTO firstCart = new CartDTO();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
        
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItem_id(2);
        itemDTO.setDescription("item 2");
        itemDTO.setCreatedAt("2017-12-16 12:35:08");
        itemDTO.setUpdatedAt("2017-12-16 12:35:08");
        
        Set<ItemDTO> itemSet = new HashSet<ItemDTO>();
        itemSet.add(itemDTO);
        
        firstCart.setItems(itemSet);
        
        List<CartDTO> cartList = new ArrayList<CartDTO>();
        cartList.add(firstCart);
        
        when(cartServiceMock.getAllCartsWithItems()).thenReturn(cartList);
 
        mockMvc.perform(get("/rest/shopping_carts/include_items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("cart 1")))
                .andExpect(jsonPath("$[0].createdAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$[0].updatedAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$[0].items", hasSize(1)))
                .andExpect(jsonPath("$[0].items[0].item_id", is(2)))
                .andExpect(jsonPath("$[0].items[0].description", is("item 2")))
                .andExpect(jsonPath("$[0].items[0].createdAt", is("2017-12-16 12:35:08")))
                .andExpect(jsonPath("$[0].items[0].updatedAt", is("2017-12-16 12:35:08")));
 
        verify(cartServiceMock, times(1)).getAllCartsWithItems();
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    /**
     * This test case is to check if cart not found
     * 
     * @throws Exception
     */
    @Test
    public void getCartById_CartNotFoundException() throws Exception {
        
    	when(cartServiceMock.getCartById(1L)).thenThrow(new CartNotFoundException(""));
 
        mockMvc.perform(get("/rest/shopping_carts/{cartId}", 1L))
                .andExpect(status().is5xxServerError());
 
        verify(cartServiceMock, times(1)).getCartById(1L);
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    
    /**
     * 	This test case is to check is single cart detail by Id is return
     * 
     * @throws Exception
     */
    @Test
    public void getCartById_CartsFound_ShouldReturnFoundSingleCartDetails() throws Exception {
        CartDTO firstCart = new CartDTO();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
        
        
        
        when(cartServiceMock.getCartById(1)).thenReturn(firstCart);
 
        mockMvc.perform(get("/rest/shopping_carts/{cartId}",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("cart 1")))
                .andExpect(jsonPath("$.createdAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$.updatedAt", is("2017-12-15 12:35:08")));
 
        verify(cartServiceMock, times(1)).getCartById(1);
        verifyNoMoreInteractions(cartServiceMock);
    }
	
    
    /**
     *  To check Add Cart 
     * 
     * @throws Exception
     */
    @Test
    public void addCart_CartsFound_ShouldReturnFoundSingleCartDetails() throws Exception {

    	CartDTO firstCart = new CartDTO();
    	firstCart.setName("cart 1");
    	
    	CartDTO addedCart = new CartDTO();
	    addedCart.setId(1);
	    addedCart.setName("cart 1");
	    addedCart.setCreatedAt("2017-12-15 12:35:08");
	    addedCart.setUpdatedAt("2017-12-15 12:35:08");
    	
    	
    	when(cartServiceMock.addCart(any (CartDTO.class))).thenReturn(1L);
    	when(cartServiceMock.getCartById(any(long.class))).thenReturn(addedCart);

        mockMvc.perform(
                post("/rest/shopping_carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(firstCart)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
		        .andExpect(jsonPath("$.id", is(1)))
		        .andExpect(jsonPath("$.name", is("cart 1")));

    }
    
    /**
     *  To check positive case of update Cart
     * 
     * @throws Exception
     */
    @Test
    public void updateCart_CartsFound_ShouldReturnFoundSingleCartDetails() throws Exception {

    	CartDTO firstCart = new CartDTO();
    	firstCart.setName("cart 2");
    	
    	CartDTO addedCart = new CartDTO();
	    addedCart.setId(1);
	    addedCart.setName("cart 2");
	    addedCart.setCreatedAt("2017-12-15 12:35:08");
	    addedCart.setUpdatedAt("2017-12-15 12:35:08");
    	
    	
    	when(cartServiceMock.updateCart(any (CartDTO.class))).thenReturn(addedCart);
    	when(cartServiceMock.getCartById(any(long.class))).thenReturn(addedCart);

        mockMvc.perform(
                put("/rest/shopping_carts/{cartId}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(firstCart)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
		        .andExpect(jsonPath("$.id", is(1)))
		        .andExpect(jsonPath("$.name", is("cart 2")))
		        .andExpect(jsonPath("$.createdAt", is("2017-12-15 12:35:08")))
		        .andExpect(jsonPath("$.updatedAt", is("2017-12-15 12:35:08")));

    }
    
    /**
     * To get an Item from Cart
     * 
     * @throws Exception
     */
    @Test
    public void getAnItemByCart_CartsFound_ShouldReturnFoundItemDetails() throws Exception {
        ItemDTO firstItem = new ItemDTO();
        firstItem.setItem_id(1);
        firstItem.setCart_id(1);
        firstItem.setDescription("item 1");
        firstItem.setCreatedAt("2017-12-15 12:35:08");
        firstItem.setUpdatedAt("2017-12-15 12:35:08");
        
        when(cartServiceMock.getAnItemFromCart(1, 1)).thenReturn(firstItem);
 
        mockMvc.perform(get("/rest/shopping_carts/{cartId}/items/{itemId}",1,1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$.item_id", is(1)))
                .andExpect(jsonPath("$.cart_id", is(1)))
                .andExpect(jsonPath("$.description", is("item 1")))
                .andExpect(jsonPath("$.createdAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$.updatedAt", is("2017-12-15 12:35:08")));
 
        verify(cartServiceMock, times(1)).getAnItemFromCart(1, 1);
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    
    /**
     * This is positive case for delete a cart
     * 
     * @throws Exception
     */
    @Test
    public void deleteCart_CartsFound_ShouldReturnMessage() throws Exception {
        
    	StatusDTO statusDTO = new StatusDTO();
    	statusDTO.setStatus(200);
    	statusDTO.setMessage("Cart Deleted Successfully");
    	
    	doNothing().when(cartServiceMock).removeCart(1L);
 
        mockMvc.perform(delete("/rest/shopping_carts/{cartId}",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("Cart Deleted Successfully")));
 
        verify(cartServiceMock, times(1)).removeCart(1L);
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    
    /**
     * This is negative case for delete a cart with CartNotFoundException if cartId is not found
     * 
     * @throws Exception
     */
    @Test
    public void deleteCart_CartsNotFoundException() throws Exception {
        
    	StatusDTO statusDTO = new StatusDTO();
    	statusDTO.setStatus(200);
    	statusDTO.setMessage("Cart Deleted Successfully");
    	
    	doThrow(new CartNotFoundException("")).when(cartServiceMock).removeCart(1L);
 
        mockMvc.perform(delete("/rest/shopping_carts/{cartId}",1))
                .andExpect(status().is5xxServerError());
 
        verify(cartServiceMock, times(1)).removeCart(1L);
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    /**
     *  To check Add Item in Cart
     * 
     * @throws Exception
     */
    @Test
    public void addItemInCart_ShouldReturnMessage() throws Exception {

    	ItemDTO itemDTO = new ItemDTO();
    	itemDTO.setDescription("new item");
    	itemDTO.setCart_id(1);
    	
    	StatusDTO statusDTO = new StatusDTO();
    	statusDTO.setStatus(200);
    	statusDTO.setMessage("Item Added Successfully");
    	
    	when(cartServiceMock.addItemInCart(any (ItemDTO.class))).thenReturn(1L);

        mockMvc.perform(
                post("/rest/shopping_carts/{cartId}/items",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("Item Added Successfully")));

    }
    
    
    /**
     *  To check , Get an Item From Cart
     * 
     * @throws Exception
     */
    @Test
    public void getItemFromCart_ShouldReturnMessage() throws Exception {

    	ItemDTO itemDTO = new ItemDTO();
    	itemDTO.setItem_id(2);
    	itemDTO.setDescription("new item");
    	itemDTO.setCart_id(1);
    	itemDTO.setCreatedAt("2017-12-15 12:35:08");
    	itemDTO.setUpdatedAt("2017-12-15 12:35:08");
    	
    	when(cartServiceMock.getAnItemFromCart(1,2)).thenReturn(itemDTO);

        mockMvc.perform(
                get("/rest/shopping_carts/{cartId}/items/{itemId}",1,2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$.item_id", is(2)))
                .andExpect(jsonPath("$.cart_id", is(1)))
                .andExpect(jsonPath("$.description", is("new item")))
                .andExpect(jsonPath("$.createdAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$.updatedAt", is("2017-12-15 12:35:08")));

    }
    
    /**
     * This is positive case for delete an item from cart
     * 
     * @throws Exception
     */
    @Test
    public void deleteItem_ItemFound_ShouldReturnMessage() throws Exception {
        
    	StatusDTO statusDTO = new StatusDTO();
    	statusDTO.setStatus(200);
    	statusDTO.setMessage("Item Deleted Successfully");
    	
    	doNothing().when(cartServiceMock).removeItem(1, 2);
 
        mockMvc.perform(delete("/rest/shopping_carts/{cartId}/items/{itemId}",1,2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("Item Deleted Successfully")));
 
        verify(cartServiceMock, times(1)).removeItem(1, 2);
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    /**
     *  this test case is to check return item list
     * @throws Exception
     */
    @Test
    public void getItemsFromCart_ItemsFound_ShouldReturnFoundItemsEntries() throws Exception {
        ItemDTO firstItem = new ItemDTO();
        firstItem.setItem_id(1);
        firstItem.setDescription("item 1");
        firstItem.setCreatedAt("2017-12-15 12:35:08");
        firstItem.setUpdatedAt("2017-12-15 12:35:08");
        firstItem.setCart_id(1);
        
        ItemDTO secondItem = new ItemDTO();
        secondItem.setItem_id(2);
        secondItem.setDescription("item 2");
        secondItem.setCreatedAt("2017-12-16 12:35:08");
        secondItem.setUpdatedAt("2017-12-16 12:35:08");
        secondItem.setCart_id(1);
        
        List<ItemDTO> itemList = new ArrayList<ItemDTO>();
        itemList.add(firstItem);
        itemList.add(secondItem);
        
        when(cartServiceMock.getItemsFromCart(1)).thenReturn(itemList);
 
        mockMvc.perform(get("/rest/shopping_carts/{cartId}/items",1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),
                        MediaType.APPLICATION_JSON.getSubtype(),                        
                        Charset.forName("utf8")                     
                        )))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].item_id", is(1)))
                .andExpect(jsonPath("$[0].description", is("item 1")))
                .andExpect(jsonPath("$[0].createdAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$[0].updatedAt", is("2017-12-15 12:35:08")))
                .andExpect(jsonPath("$[0].cart_id", is(1)))
                .andExpect(jsonPath("$[1].item_id", is(2)))
                .andExpect(jsonPath("$[1].description", is("item 2")))
                .andExpect(jsonPath("$[1].createdAt", is("2017-12-16 12:35:08")))
                .andExpect(jsonPath("$[1].updatedAt", is("2017-12-16 12:35:08")))
                .andExpect(jsonPath("$[1].cart_id", is(1)));
 
        verify(cartServiceMock, times(1)).getItemsFromCart(1);
        verifyNoMoreInteractions(cartServiceMock);
    }
    
    /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
