package com.yash.shop.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.yash.shop.model.Cart;
import com.yash.shop.model.Item;

@Repository
public class CartDAOImpl implements CartDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sf) {
		  this.sessionFactory = sf;
	}
	
	@Override
	@Transactional
	public Cart getCartById(long cartId) {
		System.out.println("--DAO cartID---"+cartId);
		Session session = sessionFactory.getCurrentSession();		
		Cart cart = (Cart) session.get(Cart.class, new Long(cartId));
		return cart;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Cart> getAllCarts() {
		Session session = sessionFactory.getCurrentSession();
		List<Cart> cartList = session.createQuery("from Cart").list();
		//List<Cart> cartList = session.createQuery("from Cart c left join fetch c.items").list();
		for(Cart c : cartList){
			System.out.println("Cart List::"+c.getItems().toArray());
		}
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
	@Transactional
	public Cart updateCart(Cart cart) {
		Session session = sessionFactory.getCurrentSession();
		session.update(cart);
		return cart;
	}

	@Override
	@Transactional
	public void removeCart(long cartId) {
		Session session = sessionFactory.getCurrentSession();
		Cart c = (Cart) session.load(Cart.class, new Long(cartId));
		if(null != c){
			session.delete(c);
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
		Item item = (Item) session.createQuery(hql)
		.setParameter("itemId", itemId)
		.setParameter("cartId", cartId)
		.uniqueResult();
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
