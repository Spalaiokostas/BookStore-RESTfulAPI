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
package com.palaiokostas.bookstore.service;

import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.repository.UserRepository;
import com.palaiokostas.bookstore.security.AuthenticationFacade;
import com.palaiokostas.bookstore.service.implementation.UserServiceImplementation;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Spyros Palaiokostas
 */


@RunWith(SpringRunner.class)
public class UserServiceTest {
    
    
    @TestConfiguration
    static class UserServiceTestContextConfig {
        
        @Bean
        public UserService userService() {
            return new UserServiceImplementation();
        }
        
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
        
        @Bean 
        public AuthenticationFacade authenticationFacade() {
            return Mockito.mock(AuthenticationFacade.class);
        }
        
    }
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Test 
    public void registerNewUserTest() {
        // given
        User user = new User();
        user.setName("username");
        user.setEmail("kafe@ilema@maindo");
        user.setPassword("toor");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        given(userRepository.save(Mockito.any(User.class))).willReturn(user);

        // when
        User registeredUser = userService.registerNewUser(user.getName(), 
                user.getEmail(), user.getPassword());
        
        // then 
        assertNotNull(registeredUser);
        assertThat(registeredUser.getName(), is(equalTo(user.getName())));
        assertThat(registeredUser.getEmail(), is(equalTo(user.getEmail())));
        assertTrue(bCryptPasswordEncoder.matches(user.getPassword(), 
                registeredUser.getPassword()));
        
    }
    
}
