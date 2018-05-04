/*
 * Copyright (C) 2018 delmanel
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
package com.palaiokostas.bookstore.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 *
 * @author Spyros Palaiokostas
 */

@Entity
@Table(name = "books")
public class Book  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    
    private String title;

    private String author;
    
    private String publisher;
    
    @Column(updatable = false, nullable = false)
    private int publication_year;
      
    /*private int rating;*/
    
    private String description;

    @Column(name = "price_cents")
    private int price;
    
    
    /* Empty constructor */
    public Book() {
    }
    
    /* Constructor used in testing */
    public Book(int id, String title, String author, String publisher, 
            int publication_year, String description, int price) 
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publication_year = publication_year;
        this.description = description;
        this.price = price;
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    
    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * @param price the price_cents to set
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the publication_year
     */
    public int getPublication_year() {
        return publication_year;
    }

    /**
     * @param publication_year the publication_year to set
     */
    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }

    /**
     * @return the rating
     *
    public int getRating() {
        return rating;
    }*/

    /**
     * @param rating the rating to set
     *
    public void setRating(int rating) {
        this.rating = rating;
    }*/

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
