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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDTO;
import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDetailsDTO;
import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.SimpleBookDTO;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 *
 * @author Spyros Palaiokostas
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })

public class BookControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;    
    
    @LocalServerPort
    private int port;
    
    @Test
    @DatabaseSetup("classpath:dbUnit/testData.xml")
    public void testListAllBooks() {
        
        // execute
        ResponseEntity<List<SimpleBookDTO>> response = restTemplate.exchange(
                "/books",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<SimpleBookDTO>>() {});
        
        // collect response
        HttpStatus status = response.getStatusCode();
        List<SimpleBookDTO> books = response.getBody();
        
        // verify
        assertThat(status, equalTo(HttpStatus.OK));
        assertNotNull(books);
        
        SimpleBookDTO book = books.get(0);
        assertThat(book.getTitle(), is(equalTo("title")));
        assertThat(book.getAuthor(), is(equalTo("author")));
        assertThat(book.getPublisher(), is(equalTo("publisher")));
        
    }
    
    @Test
    @DatabaseSetup("classpath:dbUnit/testData.xml")
    public void testGetBookDetails() {
        // execute
        ResponseEntity<BookDetailsDTO> response = restTemplate.exchange(
                "http://localhost:" + port + "/books/10",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                BookDetailsDTO.class);
        
        // collect response
        HttpStatus status = response.getStatusCode();
        BookDetailsDTO bookDetails = response.getBody();
        
        // verify
        assertThat(status, equalTo(HttpStatus.OK));
        assertNotNull(bookDetails);
        
        assertThat(bookDetails.getTitle(), is(equalTo("title")));
        assertThat(bookDetails.getAuthor(), is(equalTo("author")));
        assertThat(bookDetails.getPublisher(), is(equalTo("publisher")));
        assertThat(bookDetails.getDescription(), is(equalTo("description")));
        assertThat(bookDetails.getPrice(), is(equalTo(10000)));
        
        
    }
    
    @Test 
    @DatabaseSetup(value ="classpath:dbUnit/empty.xml")
    @ExpectedDatabase(value = "classpath:dbUnit/createdBook.xml",
            table = "books",
            assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void testCreateBook() {
        // prepare
        BookDTO book = new BookDTO();
        book.setTitle("title2");
        book.setAuthor("author2");
        book.setPublisher("publisher2");
        book.setPublication_year(1993);
        book.setDescription("description2");
        book.setPrice(100);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BookDTO> entity = new HttpEntity<>(book, headers);
        
        // execute
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port +"/books/create",
                HttpMethod.POST, 
                entity, 
                String.class);
        
        // validate
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertTrue(response.getHeaders().containsKey("Location"));
        assertThat(response.getBody(), nullValue());
    }
    
    
    
    
    
}
