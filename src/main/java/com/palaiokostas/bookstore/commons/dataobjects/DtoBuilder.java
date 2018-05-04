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
package com.palaiokostas.bookstore.commons.dataobjects;

import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDTO;
import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDetailsDTO;
import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.SimpleBookDTO;
import com.palaiokostas.bookstore.commons.dataobjects.OrderDTOs.OrderDTO;
import com.palaiokostas.bookstore.commons.dataobjects.UserDTOs.UserDTO;
import com.palaiokostas.bookstore.model.Book;
import com.palaiokostas.bookstore.model.Order;
import com.palaiokostas.bookstore.model.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

/**
 *
 * @author Spyros Palaiokostas
 */


@Component
public class DtoBuilder {
    
    private final ModelMapper modelMapper;
    
    public DtoBuilder() {
        this.modelMapper = new ModelMapper();
        
        /* set mapping stategy to strict in order to avoid wrong mappings */
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        /* add explicit mapping for OrderDTO (User -> userID), (Book -> bookID) */
        modelMapper.addMappings(new PropertyMap<Order, OrderDTO>() {
            @Override
            protected void configure() {
                map().setUserID(source.getUser().getId());
                map().setBookID(source.getBook().getId());
            }
        });
        
        /* add explicit mapping for User->name to UserDTO->username */
        modelMapper.addMappings(new PropertyMap<User, UserDTO>() {
            @Override
            protected void configure() {
                map().setUsername(source.getName());
            }
        }); 
        
        /* add explicit mapping for publication_year (int to String)    */
        modelMapper.addMappings(new PropertyMap<Book, BookDetailsDTO>() {
            @Override
            protected void configure() {
                map().setPublication_year(Integer.toString(source.getPublication_year()));
               
            }
            
        });
        
        
    }
    
    /* ----------------------------------------------------------------- */
    /* methods for mapping Book Entities to DTOs and the other way round */
    /* ----------------------------------------------------------------- */
    public List<SimpleBookDTO> toSimpleBookListDTO(List<Book> books) {
        return books.stream()
                    .map(book -> modelMapper.map(book, SimpleBookDTO.class))
                    .collect(Collectors.toList());
    }
    
    public BookDetailsDTO toBookDetailsDTO(Book book) {
        return modelMapper.map(book, BookDetailsDTO.class);
    }
    
    public Book fromBookDTO(BookDTO bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
    
    /* ----------------------------------------------------------------- */
    /* methods for mapping User Entities to DTOs and the other way round */
    /* ----------------------------------------------------------------- */
    public UserDTO toUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    
    /* ------------------------------------------------------------------ */
    /* methods for mapping Order Entities to DTOs and the other way round */
    /* ------------------------------------------------------------------ */
    public List<OrderDTO> toOrderListDTO(List<Order> orders) {
        return orders.stream()
                    .map(order -> modelMapper.map(order, OrderDTO.class))
                    .collect(Collectors.toList());
    }
    
    public OrderDTO toOrderDTO(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
    
    
    
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
 
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
}
    
}
