package com.yash.shop.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.service.CartService;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

	private MockMvc mockMvc;
	 
	@Mock
    private CartService cartServiceMock;
    
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
    public void findAll_CartsFound_ShouldReturnFoundCartsEntries() throws Exception {
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
     * This test case is to check if cart not found
     * 
     * @throws Exception
     */
    @Test
    @Ignore
    public void getCartById_CartNotFoundException() throws Exception {
        when(cartServiceMock.getCartById(1L)).thenThrow(new CartNotFoundException(""));
 
        mockMvc.perform(get("/rest/shopping_carts/{cartId}", 1L))
                .andExpect(status().isNotFound());
 
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
    @Ignore
    public void addCart_CartsFound_ShouldReturnFoundSingleCartDetails() throws Exception {
        CartDTO firstCart = new CartDTO();
        firstCart.setName("cart 1");
        
        CartDTO addedCart = new CartDTO();
        addedCart.setId(1);
        addedCart.setName("cart 1");
        addedCart.setCreatedAt("2017-12-15 12:35:08");
        addedCart.setUpdatedAt("2017-12-15 12:35:08");
        
        
        
        
        when(cartServiceMock.addCart(any(CartDTO.class))).thenReturn(1L);
        when(cartServiceMock.getCartById(1L)).thenReturn(addedCart);
 
        mockMvc.perform(post("/rest/shopping_carts")
        		.contentType(new MediaType(MediaType.APPLICATION_JSON.getType(),MediaType.APPLICATION_JSON.getSubtype(),Charset.forName("utf8")))
                .content(convertObjectToJsonBytes(firstCart))
        		)
                .andExpect(status().isOk());
 
        ArgumentCaptor<CartDTO> dtoCaptor = ArgumentCaptor.forClass(CartDTO.class);
        //verify(cartServiceMock, times(2)).addCart(dtoCaptor.capture());
        //verifyNoMoreInteractions(cartServiceMock);
        
        CartDTO dtoArgument = dtoCaptor.getValue();
        assertThat(dtoArgument.getName(), is("cart 1"));
        assertThat(dtoArgument.getCreatedAt(), is("2017-12-15 12:35:08"));
        assertThat(dtoArgument.getUpdatedAt(), is("2017-12-15 12:35:08"));
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
    
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsBytes(object);
    }
}
