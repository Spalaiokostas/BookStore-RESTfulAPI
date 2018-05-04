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
import com.palaiokostas.bookstore.model.Book;
import com.palaiokostas.bookstore.service.BookService;
import java.text.DecimalFormat;
import static java.util.Collections.singletonList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


/**
 *
 * @author Spyros Palaiokostas
 */

@RunWith(SpringRunner.class)
/* we run with secure=false opiton because we are unit testing the          **
** controller's funtionality independently of the authentication process.   **
** Authentication and Authorization will be tested upon integration testing **
** stages */
@WebMvcTest(controllers = BookController.class, secure=false)
@Import(DtoBuilder.class)
public class BookControllerTest {
    
    @MockBean
    private BookService bookService;
    
    @Autowired
    private BookController bookController;
    
    @Autowired
    private MockMvc mvc;
            
    private Book testBook;
    
    @Before
    public void setup() {
        testBook = new Book(12, "Smartass Title", "Useless Author", 
                "Phony Publisher", 1992, "Deceiving Description", 12345);
    }
    
    @Test
    public void controllerInitializedCorrectly() {
        assertThat(bookController).isNotNull();
    }
    
    @Test
    public void testListAllBooks() throws Exception {
        
        // given
        List<Book> testBooks = singletonList(testBook);
        given(bookService.getAllBooks()).willReturn(testBooks);
        
        // when
        mvc.perform(MockMvcRequestBuilders.get("/books").accept(MediaType.APPLICATION_JSON))
                
        // expect
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(Integer.toString(testBook.getId())))
                .andExpect(jsonPath("$[0].title").value(testBook.getTitle()))
                .andExpect(jsonPath("$[0].author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$[0].publisher").value(testBook.getPublisher()));
    }
    
    @Test
    public void testGetBookDetails() throws Exception {
        
        //ginen
        given(bookService.findBookByID(Mockito.anyInt())).willReturn(testBook);
        
        // when
        mvc.perform(MockMvcRequestBuilders.get("/books/{id}", testBook.getId())
        .accept(MediaType.APPLICATION_JSON))
                 
        // expect
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(Integer.toString(testBook.getId())))
                .andExpect(jsonPath("$.title").value(testBook.getTitle()))
                .andExpect(jsonPath("$.author").value(testBook.getAuthor()))
                .andExpect(jsonPath("$.publisher").value(testBook.getPublisher()))
                .andExpect(jsonPath("$.publication_year").value(testBook.getPublication_year()))
                .andExpect(jsonPath("$.description").value(testBook.getDescription()))
                .andExpect(jsonPath("$.price").value(Integer.toString(testBook.getPrice())));

    }
    
    
    
        
    
}
