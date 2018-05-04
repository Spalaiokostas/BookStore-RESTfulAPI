/*
 * Copyright (C) 2018 Spyros Palaiokostas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.palaiokostas.bookstore.service;

import com.palaiokostas.bookstore.model.Book;
import com.palaiokostas.bookstore.model.Order;
import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.model.dataTypes.OrderStatus;
import com.palaiokostas.bookstore.repository.OrderRepository;
import com.palaiokostas.bookstore.service.implementation.OrderServiceImplementation;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Spyros Palaiokostas
 */
@RunWith(SpringRunner.class)
public class OrderServiceTest {
    
    @TestConfiguration
    static class OrderServiceTestConfig {
        
        @Bean 
        public OrderService orderService() {
            return new OrderServiceImplementation();
        }
        
    }
    
    @Autowired
    private OrderService orderService;
    
    @MockBean
    private OrderRepository orderRepository;
    
    @MockBean
    private UserService userService;
            
    @MockBean
    private BookService bookService;
    
    @Test
    public void testFindOrderById() {
        Order order = new Order();
        order.setStatus(OrderStatus.UNPROCESSED);
        given(orderRepository.findById(Mockito.anyInt())).willReturn(order);
        
        Order retrievedOrder = orderService.findOrderById(order.getId());
        
        assertNotNull(retrievedOrder);
    }
    
    @Test
    public void testOrderBook() {
        // given
        Book testBook = new Book();
        testBook.setTitle("fakeTitle");
        User testUser = new User();
        testUser.setName("fakeUsername");
        Order testOrder = new Order();
        testOrder.setQuantity(1);
        given(userService.getAuthenticatedUser()).willReturn(testUser);
        given(bookService.findBookByID(Mockito.anyInt())).willReturn(testBook);
        given(orderRepository.save(Mockito.any(Order.class))).willReturn(testOrder);
        
        // when
        Order createdOrder = orderService.orderBook(testUser, testBook.getId(), 1);
        
        // expect
        assertNotNull(createdOrder);
            
    }
    
}
