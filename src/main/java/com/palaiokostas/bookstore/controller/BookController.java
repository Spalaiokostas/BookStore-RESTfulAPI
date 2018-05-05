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

import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDTO;
import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.BookDetailsDTO;
import com.palaiokostas.bookstore.commons.dataobjects.BookDTOs.SimpleBookDTO;
import com.palaiokostas.bookstore.commons.dataobjects.DtoBuilder;
import com.palaiokostas.bookstore.model.Book;
import com.palaiokostas.bookstore.service.BookService;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Spyros Palaiokostas
 */

@RestController
@RequestMapping("/books")
public class BookController {
   
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private DtoBuilder dtoBuilder;
    

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SimpleBookDTO>> listAllBooks() {

        /* call service layer */
        List<Book> books = bookService.getAllBooks();
            
        /* convert Entities to DTOs for tansfer */
        List<SimpleBookDTO> booksDTO = dtoBuilder.toSimpleBookListDTO(books);
            
        /* construct response */
        return new ResponseEntity<>(booksDTO, HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDetailsDTO> getBookDetails(@PathVariable("id") int bookID) {
            /* call service layer */
        Book book = bookService.findBookByID(bookID);
            
            /* convert to DTO */
        BookDetailsDTO bookDetails = dtoBuilder.toBookDetailsDTO(book);
            
            /* construst response */
        return new ResponseEntity<>(bookDetails, HttpStatus.OK);
    }
    
    
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createBook(@RequestBody BookDTO bookDTO) {
        Book book = dtoBuilder.fromBookDTO(bookDTO);
        Book createdBook = bookService.createNewBook(book);
            
        UriComponents location = UriComponentsBuilder.
                    fromUriString("/books/{id}")
                    .buildAndExpand(book.getId());
        
        return ResponseEntity.created(location.toUri()).build();
        
    }
            
}
