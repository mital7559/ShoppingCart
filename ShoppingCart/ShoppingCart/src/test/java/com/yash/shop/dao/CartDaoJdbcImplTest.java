package com.yash.shop.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

@RunWith(MockitoJUnitRunner.class)
public class CartDaoJdbcImplTest {

	private MockMvc mockMvc;
	 
	@Mock
	private JdbcTemplate jdbcTemplate;
	
	@Mock
	BeanPropertyRowMapper beanPropertyRowMapper;
	
	
	@InjectMocks
	private CartDaoJdbcImpl cartDaoJdbcImpl;
    
	@Before
    public void setUp() {
        
		mockMvc = MockMvcBuilders.standaloneSetup(new CartDaoJdbcImpl())
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
	     * 	This test case is to check is single cart detail by Id is return
	     * 
	     * @throws Exception
	     */
	    @SuppressWarnings("unchecked")
		@Test
	    public void getCartById_CartsFound_ShouldReturnFoundSingleCartDetails() throws Exception {
	        
	    	Cart firstCart = new Cart();
	        firstCart.setId(1);
	        firstCart.setName("cart 1");
	        firstCart.setCreatedAt("2017-12-15 12:35:08");
	        firstCart.setUpdatedAt("2017-12-15 12:35:08");
	        
	        Cart resultCart = new Cart();
	        
	        when((Cart) jdbcTemplate.queryForObject(anyString(), anyObject(),any(BeanPropertyRowMapper.class))).thenReturn(firstCart);
	 
	        resultCart = cartDaoJdbcImpl.getCartById(1);
	        
	        assertThat(resultCart.getId(), is(1L));
	        assertThat(resultCart.getName(), is("cart 1"));
	        assertThat(resultCart.getCreatedAt(), is("2017-12-15 12:35:08"));
	        assertThat(resultCart.getUpdatedAt(), is("2017-12-15 12:35:08"));

	    }
	    
	    
	    /**
	     * 	This test case is to check if cart not found then throw CartNotFoundException
	     * 
	     * @throws Exception
	     */
	    @Test(expected=CartNotFoundException.class)
	    public void getCartById_CartNotFoundException() throws Exception {
	        
	        when((Cart) jdbcTemplate.queryForObject(anyString(), anyObject(),any(BeanPropertyRowMapper.class))).thenReturn(null);
	 
	        cartDaoJdbcImpl.getCartById(1L);
	        
	    }
	    
	    /**
	     * This is to check get All carts details with All Items
	     * 
	     * @throws Exception
	     */
	    @SuppressWarnings("unchecked")
		@Test
	    public void getAllCarts_success() throws Exception{
	    	
	    	Cart firstCart = new Cart();
	        firstCart.setId(1);
	        firstCart.setName("cart 1");
	        firstCart.setCreatedAt("2017-12-15 12:35:08");
	        firstCart.setUpdatedAt("2017-12-15 12:35:08");
	        
	        Item itemDTO = new Item();
	        itemDTO.setItem_id(2);
	        itemDTO.setDescription("item 2");
	        itemDTO.setCreatedAt("2017-12-16 12:35:08");
	        itemDTO.setUpdatedAt("2017-12-16 12:35:08");
	        
	        Set<Item> itemSet = new HashSet<Item>();
	        itemSet.add(itemDTO);
	        
	        firstCart.setItems(itemSet);
	        
	        List<Cart> cartList = new ArrayList<Cart>();
	        cartList.add(firstCart);
	        
	        when((List<Cart>) jdbcTemplate.query(anyString(),any(BeanPropertyRowMapper.class))).thenReturn(cartList);
	    	
	        List<Cart> cartResultList = cartDaoJdbcImpl.getAllCarts();
	    	
	    	assertThat(cartResultList.size() , is(1));
	    }
	    
	    
	    /**
	     * This test case is to check Add a Cart
	     */
	    @SuppressWarnings("unchecked")
		@Test
	    public void addCart_test()
	    {
	    	Cart cart = new Cart();
	    	cart.setId(1L);
	    	Long cartId;
	    	
	    	when(jdbcTemplate.update(anyString(),anyString())).thenReturn(1);
	    	when((Cart) jdbcTemplate.queryForObject(anyString(), any(BeanPropertyRowMapper.class))).thenReturn(cart);
	    	
	    	cartId = cartDaoJdbcImpl.addCart(cart);
	    	
	    	assertThat(cartId , is(1L));
	    	
	    }
	    
	    /**
	     * This test case is to check Add a Cart
	     * @throws CartNotFoundException 
	     */
	    @Test
	    public void updateCart_test() throws CartNotFoundException
	    {
	    	Cart cart = new Cart();
	    	cart.setId(1);
	    	
	    	when(jdbcTemplate.update(anyString(),anyString(),anyLong())).thenReturn(1);
	    	
	    	cart = cartDaoJdbcImpl.updateCart(cart);
	    	
	    	assertThat(cart.getId() , is(1L));
	    	
	    }
	    
	    
	    /**
	     * 	This test case is to remove cart
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void removeCart_Success() throws Exception {
	        
	    	Cart firstCart = new Cart();
	        firstCart.setId(1);
	        firstCart.setName("cart 1");
	        firstCart.setCreatedAt("2017-12-15 12:35:08");
	        firstCart.setUpdatedAt("2017-12-15 12:35:08");
	        
	        when(jdbcTemplate.update(anyString(),anyLong())).thenReturn(1);
	        when(jdbcTemplate.update(anyString(),anyLong())).thenReturn(1);
	        
	        cartDaoJdbcImpl.removeCart(1L);
	        
	        assertThat(firstCart.getId(), is(1L));
	        assertThat(firstCart.getName(), is("cart 1"));
	        assertThat(firstCart.getCreatedAt(), is("2017-12-15 12:35:08"));
	        assertThat(firstCart.getUpdatedAt(), is("2017-12-15 12:35:08"));

	    }
	    
	    
	    /**
	     * This it to test add Item in Cart
	     */
	    @Test
	    public void addItemInCart_success(){
	    	
	    	Item item = new Item();
	    	long itemId;
	    	
	    	when(jdbcTemplate.update(anyString(),anyString(),anyString(),anyLong())).thenReturn(1);
	    	
	    	itemId = cartDaoJdbcImpl.addItemInCart(item);
	    	
	    	assertThat(itemId,is(1L));
	    }
	    
	    
	    /**
	     * This it to test Get an Item from Cart
	     */
	    @Test
	    public void getAnItemFromCart_success(){
	    	
	    	Item item = new Item();
	    	item.setItem_id(1L);
	    	item.setCart_id(1L);
	    	item.setDescription("item 1");
	    	
	    	when((Item) jdbcTemplate.queryForObject(anyString(),anyObject(), any(BeanPropertyRowMapper.class))).thenReturn(item);
	    	
	    	
	    	Item resultItem = cartDaoJdbcImpl.getAnItemFromCart(1L,1L);
	    	
	    	assertThat(resultItem.getItem_id(),is(1L));
	    	assertThat(resultItem.getCart_id(),is(1L));
	    	assertThat(resultItem.getDescription(), is("item 1"));
	    }
	    
	    /**
	     * 	This test case is to remove item
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void removeItem_Success() throws Exception {
	        
	        when(jdbcTemplate.update(anyString(),anyLong(),anyLong())).thenReturn(1);
	        
	        cartDaoJdbcImpl.removeItem(1L, 1L);

	    }
	    
	    /**
	     * This is to check get get Items From Cart
	     * 
	     * @throws Exception
	     */
	    @SuppressWarnings("unchecked")
		@Test
	    public void getItemsFromCart_success() throws Exception{
	    	
	    	Item firstItem = new Item();
	    	firstItem.setItem_id(1);
	    	firstItem.setCart_id(2);
	    	firstItem.setDescription("item 1");
	    	firstItem.setCreatedAt("2017-12-15 12:35:08");
	    	firstItem.setUpdatedAt("2017-12-15 12:35:08");
	        
	        Item secondItem = new Item();
	        secondItem.setItem_id(2);
	        secondItem.setCart_id(2);
	        secondItem.setDescription("item 2");
	        secondItem.setCreatedAt("2017-12-16 12:35:08");
	        secondItem.setUpdatedAt("2017-12-16 12:35:08");
	        
	        List<Item> itemList = new ArrayList<Item>();
	        itemList.add(firstItem);
	        itemList.add(secondItem);
	        
	        when((List<Item>) jdbcTemplate.query(anyString(),any(BeanPropertyRowMapper.class))).thenReturn(itemList);
	    	cartDaoJdbcImpl.getItemsFromCart(2);
	    	
	    	assertThat(itemList.size() , is(2));
	    }
	    
	    
}
