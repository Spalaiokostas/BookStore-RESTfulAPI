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

import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDTO;
import com.palaiokostas.bookstore.model.Book;
import com.palaiokostas.bookstore.repository.BookRepository;
import com.palaiokostas.bookstore.service.implementation.BookServiceImplementation;
import static java.util.Collections.singletonList;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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
public class BookServiceTest {
    
    @TestConfiguration
    static class BookServiceTestContextConfig{
        
        @Bean
        public BookService bookService() {
            return new BookServiceImplementation();
        }
    }
    
    @Autowired
    private BookService bookService;
   
    @MockBean
    private BookRepository bookRepository;
    
 
    @Test
    public void testGetAllBooks() {
        // given
        Book testBook = new Book();
        testBook.setTitle("Fake Book");
        given(bookRepository.findAll()).willReturn(singletonList(testBook));
        
        //when 
        List<Book> foundBooks = bookService.getAllBooks();
        
        // expect
        assertNotNull(foundBooks);
    }
    
    @Test
    public void testFindBookById() {
        // given 
        Book testBook = new Book();
        testBook.setTitle("title");
        given(bookRepository.findById(Mockito.anyInt())).willReturn(testBook);
        
        // when
        Book book = bookService.findBookByID(0);
        
        // expect
        assertNotNull(book);
        assertThat(book.getTitle(), is(equalTo(testBook.getTitle())));
    }
    
    @Test
    public void testCreateBook() {
        // given
        Book book = new Book();
        book.setTitle("title");
        book.setAuthor("author");
        book.setPublisher("publisher");
        book.setPublication_year(1991);
        book.setDescription("descr");
        book.setPrice(100);
        
        given(bookRepository.save(Mockito.any(Book.class))).willReturn(book);
        
        // when
        Book createdBook = bookService.createNewBook(book);
        
        assertNotNull(book);
        assertThat(createdBook.getTitle(), is(equalTo(book.getTitle())));
    }
       
}
