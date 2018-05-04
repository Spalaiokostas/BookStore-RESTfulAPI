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
package com.palaiokostas.bookstore.commons.dataobjects.OrderDTOs;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Spyros Palaiokostas
 */
public class OrderDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int userID;
    private int bookID;
    private int quantity;
    private Date submission_date;
    private String status;
    
    public OrderDTO() {
        
    } 

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userId the userID to set
     */
    public void setUserID(int userId) {
        this.userID = userId;
    }
    
    /**
     * @return the bookID
     */
    public int getBookID() {
        return bookID;
    }

    /**
     * @param bookId the bookID to set
     */
    public void setBookID(int bookId) {
        this.bookID = bookId;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the submitted
     */
    public Date getSubmission_date() {
        return submission_date;
    }

    /**
     * @param submitted the submitted to set
     */
    public void setSubmission_date(Date submitted) {
        this.submission_date = submitted;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
    
}
