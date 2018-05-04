/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.palaiokostas.bookstore.repository;

import com.palaiokostas.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Spyros Palaiokostas
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    
    User findByEmail(String email);
    User findByName(String username);
    User findById(int id);
    
}
