/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.palaiokostas.bookstore.repository;

import com.palaiokostas.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Spyros Palaiokostas
 */
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    Book findById(int id);
    
}
