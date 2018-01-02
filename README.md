# ShoppingCart Rest APIs

# Shopping Carts

<ul>
<li>GET     &nbsp;<b>ShoppingCart/rest/shopping_carts</b>&nbsp;                           Get a list of all shopping carts</li>
<li>POST    &nbsp;<b>ShoppingCart/rest/shopping_carts</b>&nbsp;                           Create a new shopping cart</li>
<li>GET     &nbsp;<b>ShoppingCart/rest/shopping_carts/[cart_id]</b>&nbsp;                 Get a specific shopping cart</li>
<li>PUT     &nbsp;<b>ShoppingCart/rest/shopping_carts/[cart_id]</b>&nbsp;                 Update a shopping cart</li>
<li>DELETE  &nbsp;<b>ShoppingCart/rest/shopping_carts/[cart_id]</b>&nbsp;                 Delete a shopping cart</li>
<li>GET     &nbsp;<b>ShoppingCart/rest/shopping_carts/include_items</b>&nbsp;             Get All Carts with All Items included</li>
</ul><br/><br/>


# Shopping Cart Items

<ul>
<li>POST    &nbsp;<b>shopping_carts/[cart_id]/items</b>&nbsp;                       Create a new shopping cart item
<li>GET     &nbsp;<b>shopping_carts/[cart_id]/items/[item_id]</b>&nbsp;             Get a specific shopping cart item
<li>DELETE  &nbsp;<b>shopping_carts/[cart_id]/items/[item_id]</b>&nbsp;             Delete a shopping cart item
<li>GET     &nbsp;<b>shopping_carts/[cart_id]/items</b>&nbsp;                       Get All Items from Specific Cart
</ul>


# Test cases 

<ul>
<li><a href="https://github.com/mital7559/ShoppingCart/blob/master/ShoppingCart/ShoppingCart/src/test/java/com/yash/shop/controller/CartControllerTest.java">CartControllerTest.java</a></li>
<li><a href="https://github.com/mital7559/ShoppingCart/blob/master/ShoppingCart/ShoppingCart/src/test/java/com/yash/shop/dao/CartDAOImplTest.java">CartDAOImplTest.java</a></li>
<li><a href="https://github.com/mital7559/ShoppingCart/blob/master/ShoppingCart/ShoppingCart/src/test/java/com/yash/shop/service/CartServiceImplTest.java">CartServiceImplTest.java</a></li>
<li><a href="https://github.com/mital7559/ShoppingCart/blob/master/ShoppingCart/ShoppingCart/src/test/java/com/yash/shop/dao/CartDaoJdbcImplTest.java">CartDaoJdbcImplTest.java</a></li>
</ul>



