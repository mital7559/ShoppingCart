package com.yash.shop.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yash.shop.exception.CartNotFoundException;
import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

@Repository
public class CartDAOImpl implements CartDAO{

	
	private SessionFactory sessionFactory;
	
	@Autowired
	public void setSessionFactory(SessionFactory sf) {
		  this.sessionFactory = sf;
	}
	
	public CartDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public CartDAOImpl() {
		
	}
	
	
	@Override
	@Transactional(rollbackFor = {CartNotFoundException.class})
	public Cart getCartById(long cartId) throws CartNotFoundException{
		Session session = sessionFactory.getCurrentSession();		
		Cart cart = (Cart) session.get(Cart.class, new Long(cartId));
		if(cart == null)
		{
			System.out.println("In exception");
			throw new CartNotFoundException("Cart Not found for Id :"+cartId);
		}
		return cart;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Cart> getAllCarts() {
		Session session = sessionFactory.getCurrentSession();
		List<Cart> cartList = session.createQuery("from Cart").list();
		//List<Cart> cartList = session.createQuery("from Cart c left join fetch c.items").list();
		
		return cartList;
	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	@Transactional
//	public List<CartItemDTO> getAllCartsWithItemsTest() {
//		Session session = sessionFactory.getCurrentSession();
//		List<CartItemDTO> cartList = session.createQuery("from Cart c join fetch c.items").list();
//		
//		return cartList;
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Cart> getAllCartsWithItems() {
		Session session = sessionFactory.getCurrentSession();
		List<Cart> cartList = session.createQuery("from Cart c join fetch c.items").list();
		
		return cartList;
	}

	@Override
	@Transactional
	public long addCart(Cart cart) {
		Session session = sessionFactory.getCurrentSession();
		long cartId = (long) session.save(cart);
		return cartId;
		
	}

	@Override
	@Transactional(rollbackFor = {CartNotFoundException.class})
	public Cart updateCart(Cart cart) throws CartNotFoundException{
		Session session = sessionFactory.getCurrentSession();
		session.update(cart);
		return cart;
	}

	@Override
	@Transactional(rollbackFor = {CartNotFoundException.class})
	public void removeCart(long cartId) throws CartNotFoundException{
		Session session = sessionFactory.getCurrentSession();
		Cart c = (Cart) session.get(Cart.class, new Long(cartId));
		if(null != c){
			session.delete(c);
		}else{
			throw new CartNotFoundException("Cart Not Found Exception for Id: "+cartId);
		}
		
	}

	@Override
	@Transactional
	public long addItemInCart(Item item) {
		Session session = sessionFactory.getCurrentSession();
		long itemId = (long) session.save(item);
		return itemId;
	}

	@Override
	@Transactional
	public Item getAnItemFromCart(long cartId, long itemId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from Item i where i.item_id = :itemId and i.cart_id = :cartId ";
		Item item = (Item) session.createQuery(hql).setParameter("itemId", itemId).setParameter("cartId", cartId).uniqueResult();
		return item;
	}

	@Override
	@Transactional
	public void removeItem(long cartId, long itemId) {
		
		Session session = sessionFactory.getCurrentSession();
		String hql = "from Item i where i.item_id = :itemId and i.cart_id = :cartId ";
		Item item = (Item) session.createQuery(hql)
		.setParameter("itemId", itemId)
		.setParameter("cartId", cartId)
		.uniqueResult();
		if(null != item){
			String deleteHql = "delete from Item i where i.item_id = :itemId and i.cart_id = :cartId ";
			session.createQuery(deleteHql)
			.setParameter("itemId", itemId)
			.setParameter("cartId", cartId).executeUpdate();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Item> getItemsFromCart(long cartId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from Item i where i.cart_id = :cartId ";
		List<Item> itemList = session.createQuery(hql)
		.setParameter("cartId", cartId)
		.list();
		return itemList;
	}

	

}
