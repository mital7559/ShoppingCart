package com.yash.shop.dao;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

@RunWith(MockitoJUnitRunner.class)
public class CartDAOImplTest {

	private MockMvc mockMvc;
	 
	@Mock
    private SessionFactory sessionFactoryMock;
	
	@Mock
	Session session;
	
	@Mock
	Query query;
	
	@InjectMocks
	private CartDAOImpl cartDAOImpl;
    
	@Before
    public void setUp() {
        
		mockMvc = MockMvcBuilders.standaloneSetup(new CartDAOImpl(sessionFactoryMock))
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
	    @Test
	    public void getCartById_CartsFound_ShouldReturnFoundSingleCartDetails() throws Exception {
	        
	    	Cart firstCart = new Cart();
	        firstCart.setId(1);
	        firstCart.setName("cart 1");
	        firstCart.setCreatedAt("2017-12-15 12:35:08");
	        firstCart.setUpdatedAt("2017-12-15 12:35:08");
	        
	        Cart resultCart = new Cart();
	        
	        when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	        when(session.get( Cart.class, 1L)).thenReturn(firstCart);
	 
	        resultCart = cartDAOImpl.getCartById(1);
	        
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
	        
	        Cart resultCart = new Cart();
	        
	        when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	        when(session.get( Cart.class, 1L)).thenReturn(null);
	 
	        resultCart = cartDAOImpl.getCartById(1);
	        
	    }
	    
	    /**
	     * This is to check get All carts details with All Items
	     * 
	     * @throws Exception
	     */
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
	        
	        when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	    	when(session.createQuery("from Cart")).thenReturn(query);
	    	when(query.list()).thenReturn(cartList);
	    	
	    	cartDAOImpl.getAllCarts();
	    	
	    	assertThat(cartList.size() , is(1));
	    }
	    
	    /**
	     * This is to check get All carts details with All Items
	     * 
	     * @throws Exception
	     */
	    @Test
	    public void getAllCarts_getAllCartsWithItems() throws Exception{
	    	
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
	        
	        when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	    	when(session.createQuery("from Cart c join fetch c.items")).thenReturn(query);
	    	when(query.list()).thenReturn(cartList);
	    	
	    	cartDAOImpl.getAllCartsWithItems();
	    	
	    	assertThat(cartList.size() , is(1));
	    }
	    
	    
	    /**
	     * This test case is to check Add a Cart
	     */
	    @Test
	    public void addCart_test()
	    {
	    	Cart cart = new Cart();
	    	Long cartId;
	    	
	    	when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	    	when(session.save(cart)).thenReturn(1L);
	    	
	    	cartId = cartDAOImpl.addCart(cart);
	    	
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
	    	Long cartId;
	    	
	    	when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	    	doNothing().when(session).update(cart);
	    	
	    	cart = cartDAOImpl.updateCart(cart);
	    	
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
	        
	        when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	        when(session.get( Cart.class, 1L)).thenReturn(firstCart);
	        doNothing().when(session).delete(firstCart);
	        
	        cartDAOImpl.removeCart(1L);
	        
	        assertThat(firstCart.getId(), is(1L));
	        assertThat(firstCart.getName(), is("cart 1"));
	        assertThat(firstCart.getCreatedAt(), is("2017-12-15 12:35:08"));
	        assertThat(firstCart.getUpdatedAt(), is("2017-12-15 12:35:08"));

	    }
	    
	    
	    /**
	     * 	This test case is to check CartNotFoundException thrown when remove cart
	     * 
	     * @throws Exception
	     */
	    @Test(expected=CartNotFoundException.class)
	    public void removeCart_CartNotFoundException() throws Exception {
	        
	        when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	        when(session.get( Cart.class, 1L)).thenReturn(null);
	        
	        cartDAOImpl.removeCart(1L);
	        
	    }
	    
	    /**
	     * This it to test add Item in Cart
	     */
	    @Test
	    public void addItemInCart_success(){
	    	
	    	Item item = new Item();
	    	long itemId;
	    	
	    	when(sessionFactoryMock.getCurrentSession()).thenReturn(session);
	    	when(session.save(item)).thenReturn(1L);
	    	
	    	itemId = cartDAOImpl.addItemInCart(item);
	    	
	    	assertThat(itemId,is(1L));
	    }
	    
	    
}
