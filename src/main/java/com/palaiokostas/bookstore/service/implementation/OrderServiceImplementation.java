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
package com.palaiokostas.bookstore.service.implementation;

import com.palaiokostas.bookstore.model.Order;
import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.model.dataTypes.OrderStatus;
import com.palaiokostas.bookstore.repository.OrderRepository;
import com.palaiokostas.bookstore.service.BookService;
import com.palaiokostas.bookstore.service.OrderService;
import java.util.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Spyros Palaiokostas
 */
@Service
public class OrderServiceImplementation implements OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired BookService bookService;
    
    @Override
    public Order findOrderById(int id) {
        return orderRepository.findById(id);
    }
    
    @Override
    public Order orderBook(User user, int bookId,  int quantity){
        Order order = new Order();
        order.setBook(bookService.findBookByID(bookId));
        order.setUser(user);
        order.setQuantity(quantity);
        order.setStatus(OrderStatus.UNPROCESSED);
        order.setSubmission_date(new Date());
        orderRepository.save(order);
        return order;
    }

    @Override
    public List<Order> listAllOrders() {
        return orderRepository.findAll();
        
    }
    
    @Override
    public List<Order> getUserOrders(User user) {
        return orderRepository.findUserOrders(user.getId());

    }

    @Override
    public List<Order> listUnprocessedOrders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Order> listProcessedOrders() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
