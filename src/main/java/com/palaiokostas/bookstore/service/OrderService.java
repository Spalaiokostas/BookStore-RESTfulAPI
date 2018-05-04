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

import com.palaiokostas.bookstore.model.Order;
import com.palaiokostas.bookstore.model.User;
import java.util.List;

/**
 *
 * @author Spyros Palaiokostas
 */
public interface OrderService {
    
    public Order findOrderById(int id);
    
    public Order orderBook(User user, int bookId, int quantity);
    
    public List<Order> listAllOrders();
    
    public List<Order> listUnprocessedOrders();
    
    public List<Order> listProcessedOrders();
    
    public List<Order> getUserOrders(User user);
    
}
