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
package com.palaiokostas.bookstore.controller;

import com.palaiokostas.bookstore.commons.dataobjects.DtoBuilder;
import com.palaiokostas.bookstore.commons.dataobjects.OrderDTOs.OrderBookDTO;
import com.palaiokostas.bookstore.commons.dataobjects.OrderDTOs.OrderDTO;
import com.palaiokostas.bookstore.model.Order;
import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.service.OrderService;
import com.palaiokostas.bookstore.service.UserService;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Spyros Palaiokostas
 */

@RestController
@RequestMapping("/orders")
public class OrdersController {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private OrderService orderService;
    
    @Autowired 
    private UserService userService;
    
    @Autowired
    private DtoBuilder dtoBuilder;
    

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderDTO>> listUserOrders(){
        /* call service layer */
        User currentUser = userService.getAuthenticatedUser();
        List<Order> orders = orderService.getUserOrders(currentUser);
        
        /* build DTO from entities */
        List<OrderDTO> ordersDto = dtoBuilder.toOrderListDTO(orders);
        
        /* construct response */
        return new ResponseEntity<>(ordersDto, HttpStatus.OK);
    }
    
    
    @PostMapping(value="", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> purchaseBook(@RequestBody OrderBookDTO orderBookDTO) 
    {    
        int bookID = orderBookDTO.getBookId();
        int quantity;
        quantity = orderBookDTO.getQuantity();
        //try {
            
            
            /* call service layer */
            User currentUser = userService.getAuthenticatedUser();
            Order order = orderService.orderBook(currentUser, bookID, quantity);
            
            /* create Location header */
            URI location = ServletUriComponentsBuilder
                        .fromCurrentServletMapping().path("/orders/{id}").build()
                        .expand(order.getId()).toUri();
            
            /* constuct response that contains Location header with **
            ** the location of the created Order */
            return ResponseEntity.created(location).build();
        //} catch (Exception e) {
            //return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        //}
        
    }
    
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable("id") int orderID) {
        Order order = orderService.findOrderById(orderID);
        return new ResponseEntity<>(dtoBuilder.toOrderDTO(order), HttpStatus.OK);
    }
    
    
    
    
    
    
}
