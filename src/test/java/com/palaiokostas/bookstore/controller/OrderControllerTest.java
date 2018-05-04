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

import com.google.gson.Gson;
import com.palaiokostas.bookstore.commons.dataobjects.DtoBuilder;
import com.palaiokostas.bookstore.commons.dataobjects.OrderDTOs.OrderBookDTO;
import com.palaiokostas.bookstore.model.Book;
import com.palaiokostas.bookstore.model.Order;
import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.model.dataTypes.OrderStatus;
import com.palaiokostas.bookstore.model.dataTypes.UserRoles;
import com.palaiokostas.bookstore.service.OrderService;
import com.palaiokostas.bookstore.service.UserService;
import static java.util.Collections.singletonList;
import java.util.Date;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Spyros Palaiokostas
 */

@RunWith(SpringRunner.class)
/* we run with secure=false opiton because we are unit testing the          **
** controller's funtionality independently of the authentication process.   **
** Authentication and Authorization will be tested upon integration testing **
** stages */
@WebMvcTest(controllers = OrdersController.class, secure=false)
@Import(DtoBuilder.class)
public class OrderControllerTest {
    
    @MockBean
    private OrderService orderService;
    
    @Autowired
    private OrdersController ordersController;
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private MockMvc mvc;
    
    private Order testOrder;
    private Book testBook;
    private User testUser;
    
    @Before
    public void setup() {
        testBook = new Book(12, "Smartass Title", "Useless Author", 
                "Phony Publisher", 1992, "Deceiving Description", 10000000);
        testUser = new User(10, "username", "email@provider.domain", 
                "root", null, UserRoles.ROLE_USER);
        testOrder = new Order(20, testUser, testBook, 1, new Date(), OrderStatus.UNPROCESSED);
    }
    
    @Test
    public void controllerInitializedCorrectly() {
        assertThat(ordersController).isNotNull();
    }
    
    @Test
    public void testListUserOrders() throws Exception {
        // given
        List<Order> testOrders = singletonList(testOrder);
        given(userService.getAuthenticatedUser()).willReturn(testUser);
        given(orderService.getUserOrders(Mockito.any(User.class))).willReturn(testOrders);
        
        // when
        mvc.perform(MockMvcRequestBuilders.get("/orders")
            .accept(MediaType.APPLICATION_JSON))
                
                // expect
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(Integer.toString(testOrder.getId())))
                .andExpect(jsonPath("$[0].userID").value(Integer.toString(testOrder.getUser().getId())))
                .andExpect(jsonPath("$[0].bookID").value(Integer.toString(testOrder.getBook().getId())))
                .andExpect(jsonPath("$[0].quantity").value(Integer.toString(testOrder.getQuantity())))
                .andExpect((jsonPath("$[0].status").value(OrderStatus.UNPROCESSED.name())));
                
    }
    
    @Test
    public void testPurchaseBook() throws Exception {
        
        //given
        given(orderService.orderBook(Mockito.any(User.class), Mockito.anyInt(),Mockito.anyInt())).willReturn(testOrder);
        given(userService.getAuthenticatedUser()).willReturn(testUser);
        String location_url = "http://localhost/orders/"+testOrder.getId();
        
        OrderBookDTO order = new OrderBookDTO();
        order.setBookId(testBook.getId());
        order.setQuantity(testOrder.getQuantity());
        
        Gson gson = new Gson();
        String json = gson.toJson(order);
        
       
        // when
        mvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                 
                // expect
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo(location_url)));
    }
    
}
