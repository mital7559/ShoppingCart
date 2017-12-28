package com.yash.shop.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.yash.shop.dao.CartDAO;
import com.yash.shop.dao.CartDaoJdbcImpl;
import com.yash.shop.dto.CartDTO;
import com.yash.shop.dto.ItemDTO;
import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceImplTest {
	
	private MockMvc mockMvc;
	 
	@Mock
    private CartDAO cartDAOMock;
	
	@Mock
	private CartDaoJdbcImpl cartDaoJdbcMock;
	
	@InjectMocks
	private CartServiceImpl cartServiceImpl;
    
	@Before
    public void setUp() {
        
		mockMvc = MockMvcBuilders.standaloneSetup(new CartServiceImpl(cartDAOMock,cartDaoJdbcMock))
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
	 *  Service tested with Hibernate: getCartById
	 * @throws CartNotFoundException 
	 */
	@Test
	 public void getCartById_isHibernate_success() throws CartNotFoundException{
		
		Cart firstCart = new Cart();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
		
        cartServiceImpl.setConnection("hibernate");
        
		when(cartDAOMock.getCartById(1L)).thenReturn(firstCart);
		
		CartDTO cartDTO = cartServiceImpl.getCartById(1L);
		
		assertThat(cartDTO.getId(), is(1L));
		assertThat(cartDTO.getName(), is("cart 1"));
		assertThat(cartDTO.getCreatedAt() , is("2017-12-15 12:35:08"));
		assertThat(cartDTO.getUpdatedAt() , is("2017-12-15 12:35:08"));
	 }
	
	/**
	 *  Service tested with JDBC: getCartById
	 * @throws CartNotFoundException 
	 */
	@Test
	 public void getCartById_isJDBC_success() throws CartNotFoundException{
		
		Cart firstCart = new Cart();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
		
        cartServiceImpl.setConnection("jdbc");
        
		when(cartDaoJdbcMock.getCartById(1L)).thenReturn(firstCart);
		
		CartDTO cartDTO = cartServiceImpl.getCartById(1L);
		
		assertThat(cartDTO.getId(), is(1L));
		assertThat(cartDTO.getName(), is("cart 1"));
		assertThat(cartDTO.getCreatedAt() , is("2017-12-15 12:35:08"));
		assertThat(cartDTO.getUpdatedAt() , is("2017-12-15 12:35:08"));
	 }
	
	
	
	/**
	 * service tested with Hibernate: getAllCarts
	 */
	@Test
	public void getAllCarts_isHibernate_success(){
		
		List<Cart> cartList = new ArrayList<Cart>();
		
		Cart firstCart = new Cart();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
        
        Cart secondCart = new Cart();
        secondCart.setId(2);
        secondCart.setName("cart 2");
        secondCart.setCreatedAt("2017-12-16 12:35:08");
        secondCart.setUpdatedAt("2017-12-16 12:35:08");
        
        cartList.add(firstCart);
        cartList.add(secondCart);
        
        cartServiceImpl.setConnection("hibernate");
        
        when(cartDAOMock.getAllCarts()).thenReturn(cartList);
        
        List<CartDTO> cartDTOList = cartServiceImpl.getAllCarts();
        
        assertThat(cartDTOList.size() , is(2));
	}
	
	/**
	 * service tested with JDBC: getAllCarts
	 */
	@Test
	public void getAllCarts_isJDBC_success(){
		
		List<Cart> cartList = new ArrayList<Cart>();
		
		Cart firstCart = new Cart();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
        
        Cart secondCart = new Cart();
        secondCart.setId(2);
        secondCart.setName("cart 2");
        secondCart.setCreatedAt("2017-12-16 12:35:08");
        secondCart.setUpdatedAt("2017-12-16 12:35:08");
        
        cartList.add(firstCart);
        cartList.add(secondCart);
        
        cartServiceImpl.setConnection("jdbc");
        
        when(cartDaoJdbcMock.getAllCarts()).thenReturn(cartList);
        
        List<CartDTO> cartDTOList = cartServiceImpl.getAllCarts();
        
        assertThat(cartDTOList.size() , is(2));
	}
	
	
	/**
	 * Service tested : getAllCartsWithItems for no duplicate cart
	 */
	@Test
	public void getAllCartsWithItems_NoDuplicate_cart()
	{
		List<Cart> cartList = new ArrayList<Cart>();
		
		Set<Item> itemSet1 = new HashSet<Item>();
		Set<Item> itemSet2 = new HashSet<Item>();
		
		Item item1 = new Item();
		item1.setItem_id(1);
		item1.setDescription("item 1");
		item1.setCart_id(1);
		
		Item item2 = new Item();
		item2.setItem_id(2);
		item2.setDescription("item 2");
		item2.setCart_id(1);
		
		Item item3 = new Item();
		item3.setItem_id(3);
		item3.setDescription("item 3");
		item3.setCart_id(2);
		
		itemSet1.add(item1);
		itemSet1.add(item2);
		itemSet2.add(item3);
		
		
		Cart firstCart = new Cart();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
        firstCart.setItems(itemSet1);
        
        Cart secondCart = new Cart();
        secondCart.setId(2);
        secondCart.setName("cart 2");
        secondCart.setCreatedAt("2017-12-16 12:35:08");
        secondCart.setUpdatedAt("2017-12-16 12:35:08");
        secondCart.setItems(itemSet2);
        
        cartList.add(firstCart);
        cartList.add(secondCart);
        
        when(cartDAOMock.getAllCartsWithItems()).thenReturn(cartList);
        
        List<CartDTO> cartDTOList = cartServiceImpl.getAllCartsWithItems();
        
        assertThat(cartDTOList.size() , is(2));
        assertThat(cartDTOList.iterator().next().getItems().size() , is(2));
	}
	
	/**
	 * Service tested : getAllCartsWithItems for duplicate cart ignore
	 */
	@Test
	public void getAllCartsWithItemsDuplicate_cart_Ignore()
	{
		List<Cart> cartList = new ArrayList<Cart>();
		
		Set<Item> itemSet1 = new HashSet<Item>();
		Set<Item> itemSet2 = new HashSet<Item>();
		
		Item item1 = new Item();
		item1.setItem_id(1);
		item1.setDescription("item 1");
		item1.setCart_id(1);
		
		Item item2 = new Item();
		item2.setItem_id(2);
		item2.setDescription("item 2");
		item2.setCart_id(1);
		
		Item item3 = new Item();
		item3.setItem_id(3);
		item3.setDescription("item 3");
		item3.setCart_id(2);
		
		itemSet1.add(item1);
		itemSet1.add(item2);
		itemSet2.add(item3);
		
		
		Cart firstCart = new Cart();
        firstCart.setId(1);
        firstCart.setName("cart 1");
        firstCart.setCreatedAt("2017-12-15 12:35:08");
        firstCart.setUpdatedAt("2017-12-15 12:35:08");
        firstCart.setItems(itemSet1);
        
        Cart secondCart = new Cart();
        secondCart.setId(2);
        secondCart.setName("cart 2");
        secondCart.setCreatedAt("2017-12-16 12:35:08");
        secondCart.setUpdatedAt("2017-12-16 12:35:08");
        secondCart.setItems(itemSet2);
        
        Cart thirdCart = new Cart();
        thirdCart.setId(1);
        thirdCart.setName("cart 1");
        thirdCart.setCreatedAt("2017-12-15 12:35:08");
        thirdCart.setUpdatedAt("2017-12-15 12:35:08");
        thirdCart.setItems(itemSet1);
        
        cartList.add(firstCart);
        cartList.add(secondCart);
        cartList.add(thirdCart);
        
        when(cartDAOMock.getAllCartsWithItems()).thenReturn(cartList);
        
        List<CartDTO> cartDTOList = cartServiceImpl.getAllCartsWithItems();
        
        assertThat(cartDTOList.size() , is(2));
        assertThat(cartDTOList.iterator().next().getItems().size() , is(2));
	}
	
	/**
	 *  Service tested with Hibernate: addCart
	 */
	@Test
	public void addCart_isHibernate_success(){
		
		CartDTO cartDTO = new CartDTO();
		cartDTO.setName("cart 1");
	
		cartServiceImpl.setConnection("hibernate");
		
		when(cartDAOMock.addCart(any(Cart.class))).thenReturn(1L);
		
		long cartId = cartServiceImpl.addCart(cartDTO);
		
		assertThat(cartId , is(1L));
		
	}
	
	/**
	 *  Service tested with JDBC: addCart
	 */
	@Test
	public void addCart_isJDBC_success(){
		
		CartDTO cartDTO = new CartDTO();
		cartDTO.setName("cart 1");
	
		cartServiceImpl.setConnection("jdbc");
		
		when(cartDaoJdbcMock.addCart(any(Cart.class))).thenReturn(1L);
		
		long cartId = cartServiceImpl.addCart(cartDTO);
		
		assertThat(cartId , is(1L));
		
	}
	
	/**
	 *  Service tested with Hibernate: addCart
	 * @throws CartNotFoundException 
	 */
	@Test
	public void updateCart_isHibernate_success() throws CartNotFoundException{
		
		CartDTO cartDTO = new CartDTO();
		cartDTO.setName("cart 2");
		
		Cart cart = new Cart();
	
		cartServiceImpl.setConnection("hibernate");
		when(cartDAOMock.updateCart(any(Cart.class))).thenReturn(cart);
		
		cartDTO = cartServiceImpl.updateCart(cartDTO);
		
		assertThat(cartDTO.getName() , is("cart 2"));
		
	}
	
	/**
	 *  Service tested with JDBC: addCart
	 * @throws CartNotFoundException 
	 */
	@Test
	public void updateCart_isJDBC_success() throws CartNotFoundException{
		
		CartDTO cartDTO = new CartDTO();
		cartDTO.setName("cart 2");
		
		Cart cart = new Cart();
	
		cartServiceImpl.setConnection("jdbc");
		when(cartDaoJdbcMock.updateCart(any(Cart.class))).thenReturn(cart);
		
		cartDTO = cartServiceImpl.updateCart(cartDTO);
		
		assertThat(cartDTO.getName() , is("cart 2"));
		
	}
	
	/**
	 * Service tested with Hibernate: removeCart
	 * 
	 * @throws CartNotFoundException
	 */
	@Test
	public void removeCart_isHibernate() throws CartNotFoundException{
		
		cartServiceImpl.setConnection("hibernate");
		doNothing().when(cartDAOMock).removeCart(1L);
		
		cartServiceImpl.removeCart(1L);
		
	}
	
	/**
	 * Service tested with JDBC: removeCart
	 * 
	 * @throws CartNotFoundException
	 */
	@Test
	public void removeCart_isJDBC() throws CartNotFoundException{
		
		cartServiceImpl.setConnection("jdbc");
		doNothing().when(cartDaoJdbcMock).removeCart(1L);
		
		cartServiceImpl.removeCart(1L);
		
	}
	
	/**
	 * Service tested with hibernate: addItemInCart
	 */
	@Test
	public void addItemInCart_isHibernate_success(){
		
		ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItem_id(2);
        itemDTO.setDescription("item 2");
        itemDTO.setCreatedAt("2017-12-16 12:35:08");
        itemDTO.setUpdatedAt("2017-12-16 12:35:08");
        
        cartServiceImpl.setConnection("hibernate");
        when(cartDAOMock.addItemInCart(any (Item.class))).thenReturn(1L);
        
        long itemId = cartServiceImpl.addItemInCart(itemDTO);
        
        assertThat(itemId , is(1L));
		
	}
	
	/**
	 * Service tested with JDBC: addItemInCart
	 */
	@Test
	public void addItemInCart_isJDBC_success(){
		
		ItemDTO itemDTO = new ItemDTO();
        itemDTO.setItem_id(2);
        itemDTO.setDescription("item 2");
        itemDTO.setCreatedAt("2017-12-16 12:35:08");
        itemDTO.setUpdatedAt("2017-12-16 12:35:08");
        
        cartServiceImpl.setConnection("jdbc");
        when(cartDaoJdbcMock.addItemInCart(any (Item.class))).thenReturn(1L);
        
        long itemId = cartServiceImpl.addItemInCart(itemDTO);
        
        assertThat(itemId , is(1L));
		
	}
	
	
	/**
	 * Service tested with Hibernate: getAnItemFromCart
	 */
	@Test
	public void getAnItemFromCart_isHibernate_success(){
		
		Item item = new Item();
        item.setItem_id(2);
        item.setDescription("item 2");
        item.setCreatedAt("2017-12-16 12:35:08");
        item.setUpdatedAt("2017-12-16 12:35:08");
        
        cartServiceImpl.setConnection("hibernate");
        when(cartDAOMock.getAnItemFromCart(1L, 1L)).thenReturn(item);
        
        ItemDTO itemDTO = cartServiceImpl.getAnItemFromCart(1L, 1L);
        
        assertThat(itemDTO.getItem_id() , is(2L));
        assertThat(itemDTO.getDescription(), is("item 2"));
		
	}
	
	/**
	 * Service tested with JDBC: getAnItemFromCart
	 */
	@Test
	public void getAnItemFromCart_isJDBC_success(){
		
		Item item = new Item();
        item.setItem_id(2);
        item.setDescription("item 2");
        item.setCreatedAt("2017-12-16 12:35:08");
        item.setUpdatedAt("2017-12-16 12:35:08");
        
        
        cartServiceImpl.setConnection("jdbc");
        when(cartDaoJdbcMock.getAnItemFromCart(1L, 1L)).thenReturn(item);
        
        ItemDTO itemDTO = cartServiceImpl.getAnItemFromCart(1L, 1L);
        
        assertThat(itemDTO.getItem_id() , is(2L));
        assertThat(itemDTO.getDescription(), is("item 2"));
		
	}
	
	/**
	 * Service tested with Hibernate: removeItem
	 * 
	 * @throws CartNotFoundException
	 */
	@Test
	public void removeItem_isHibernate_success() throws CartNotFoundException{
		
		cartServiceImpl.setConnection("hibernate");
		doNothing().when(cartDAOMock).removeItem(1L, 1L);
		
		cartServiceImpl.removeItem(1L, 1L);
		
	}
	
	
	/**
	 * Service tested with JDBC: removeItem
	 * 
	 * @throws CartNotFoundException
	 */
	@Test
	public void removeItem_isJDBC_success() throws CartNotFoundException{
		
		cartServiceImpl.setConnection("jdbc");
		doNothing().when(cartDaoJdbcMock).removeItem(1L, 1L);
		
		cartServiceImpl.removeItem(1L, 1L);
		
	}
	
	
	
	/**
	 * Service tested with Hibernate: getItemsFromCart
	 */
	@Test 
	public void getItemsFromCart_isHibernate_success(){
		
		List<Item> itemList = new ArrayList<Item>();
		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
		
		Item item = new Item();
        item.setItem_id(2);
        item.setDescription("item 2");
        item.setCreatedAt("2017-12-16 12:35:08");
        item.setUpdatedAt("2017-12-16 12:35:08");
        
        itemList.add(item);
        
        cartServiceImpl.setConnection("hibernate");
        when(cartDAOMock.getItemsFromCart(1L)).thenReturn(itemList);
        
        itemDTOList = cartServiceImpl.getItemsFromCart(1L);
        
        assertThat(itemDTOList.size() , is (1));
        assertThat(itemDTOList.iterator().next().getDescription() , is("item 2"));
	}
	
	/**
	 * Service tested with JDBC: getItemsFromCart
	 */
	@Test 
	public void getItemsFromCart_isJDBC_success(){
		
		List<Item> itemList = new ArrayList<Item>();
		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
		
		Item item = new Item();
        item.setItem_id(2);
        item.setDescription("item 2");
        item.setCreatedAt("2017-12-16 12:35:08");
        item.setUpdatedAt("2017-12-16 12:35:08");
        
        itemList.add(item);
        
        cartServiceImpl.setConnection("jdbc");
        when(cartDaoJdbcMock.getItemsFromCart(1L)).thenReturn(itemList);
        
        itemDTOList = cartServiceImpl.getItemsFromCart(1L);
        
        assertThat(itemDTOList.size() , is (1));
        assertThat(itemDTOList.iterator().next().getDescription() , is("item 2"));
	}
}
