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

package com.palaiokostas.bookstore.service.implementation;

import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.repository.UserRepository;
import com.palaiokostas.bookstore.security.AuthenticationFacade;
import com.palaiokostas.bookstore.security.MyUserDetails;
import com.palaiokostas.bookstore.model.dataTypes.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.palaiokostas.bookstore.service.UserService;
import org.springframework.security.core.Authentication;

/**
 *
 * @author Spyros Palaiokostas
 */

@Service
public class UserServiceImplementation implements UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticationFacade authenticationFacade;
    
    public UserServiceImplementation() {
        
    }
    
    @Override
    public User registerNewUser(String username, String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setName(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRoles.ROLE_USER);
        userRepository.save(user);
        return user;
    }
    
    
    @Override
    public User findUserByID(int userID) {
        User user = userRepository.findById(userID);
        return user;
    }
    
    
    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = authenticationFacade.getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
    
}
