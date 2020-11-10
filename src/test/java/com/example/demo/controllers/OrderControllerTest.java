package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(1L);

        Item item = new Item();
        item.setId(2L);
        item.setName("newItem");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("newDescription");
        List<Item> listItems = new ArrayList<>();
        listItems.add(item);

        Cart cart = new Cart();
        cart.setId(3L);
        cart.setItems(listItems);
        cart.setTotal(BigDecimal.ONE);
        cart.setUser(user);

        user.setCart(cart);

        UserOrder order = new UserOrder();
        order.setId(4L);
        order.setUser(user);
        order.setItems(listItems);
        order.setTotal(BigDecimal.ONE);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(UserOrder.createFromCart(cart)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder result = response.getBody();
        assertEquals(user, result.getUser());
        assertEquals(listItems, result.getItems());
        assertEquals(BigDecimal.ONE, result.getTotal());
    }
}
