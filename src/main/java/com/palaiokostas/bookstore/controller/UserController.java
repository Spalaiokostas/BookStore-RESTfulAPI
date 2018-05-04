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
import com.palaiokostas.bookstore.commons.dataobjects.UserDTOs.AccountDTO;
import com.palaiokostas.bookstore.commons.dataobjects.UserDTOs.UserDTO;
import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Spyros Palaiokostas
 */

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DtoBuilder dtoBuilder;
    
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerUser(@RequestBody AccountDTO accountDTO) {
        //try {
            /* call service layer */
            User user = userService.registerNewUser(accountDTO.getUsername(),
                    accountDTO.getEmail(), accountDTO.getPassword());
        
            /* construct response */
            return new ResponseEntity<>(HttpStatus.CREATED);
        //} catch (Exception e) {
            //log.error(e.getMessage());
            //return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        //}
        
    }
    
    
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> showProfile() {
        //try { 
            /* call service layer */
            User user = userService.getAuthenticatedUser();
            
            /* build DTO */
            UserDTO userDTO = dtoBuilder.toUserDTO(user);
            
            /* construct response */
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
            
        //} catch (Exception e) {
            //log.error(e.getMessage());
            //return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        //}
    }
    
}
