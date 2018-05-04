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

import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDTO;
import com.palaiokostas.bookstore.model.Book;
import com.palaiokostas.bookstore.model.Order;
import com.palaiokostas.bookstore.repository.BookRepository;
import com.palaiokostas.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.palaiokostas.bookstore.service.BookService;
import com.palaiokostas.bookstore.service.UserService;
import java.util.List;

/**
 *
 * @author Spyros Palaiokostas
 */

@Service
public class BookServiceImplementation implements BookService {
    
    @Autowired
    private BookRepository bookRepository;  
    
    public BookServiceImplementation() {
        
    }
    
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    @Override
    public Book findBookByID(int id) {
        return bookRepository.findById(id);
    }
    
    @Override
    public Book createNewBook(Book book) {
        /*Book book = new Book();
        book.setAuthor(bookDTO.getAuthor());
        book.setTitle(bookDTO.getTitle());
        book.setPrice(bookDTO.getPrice());
        book.setPublisher(bookDTO.getPublisher());
        book.setPublication_year(bookDTO.getPublication_year());
        book.setDescription(bookDTO.getDescription());*/
        
        return bookRepository.save(book);
    }
    
    
}
