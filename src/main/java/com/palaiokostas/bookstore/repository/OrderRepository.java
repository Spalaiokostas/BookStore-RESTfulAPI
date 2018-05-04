/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.palaiokostas.bookstore.repository;

import com.palaiokostas.bookstore.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Spyros Palaiokostas
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    
    @Query("select o "
                    + "from Order o "
                    + "where o.user.id = :userId"
    )
    List<Order> findUserOrders(@Param("userId") Integer id);
   
    Order findById(int ID);
}
