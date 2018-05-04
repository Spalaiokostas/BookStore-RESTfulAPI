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

import com.google.gson.Gson;
import com.palaiokostas.bookstore.commons.dataobjects.DtoBuilder;
import com.palaiokostas.bookstore.commons.dataobjects.UserDTOs.AccountDTO;
import com.palaiokostas.bookstore.model.User;
import com.palaiokostas.bookstore.model.dataTypes.UserRoles;
import com.palaiokostas.bookstore.service.UserService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.BDDMockito.given;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Spyros Palaiokostas
 */

@RunWith(SpringRunner.class)
/* we run with secure=false opiton because we are unit testing the          **
** controller's funtionality independently of the authentication process.   **
** Authentication and Authorization will be tested upon integration testing **
** stages */
@WebMvcTest(controllers = UserController.class, secure=false)
@Import(DtoBuilder.class)
public class UserControllerTest {
    
    @MockBean
    private UserService userService;
    
    @Autowired
    private UserController userController;
    
    @Autowired
    private MockMvc mvc;
    
    private User testUser;
    
    @Before
    public void setup() {
        testUser = new User(10, "username", "email@provider.domain", 
                "root", null, UserRoles.ROLE_USER);
    }
    
    
    @Test
    public void controllerInitializedCorrectly() {
        assertThat(userController).isNotNull();
    }
    
    @Test
    public void testShowProfile() throws Exception {
        // given
        given(userService.getAuthenticatedUser()).willReturn(testUser);
        
        // when
        mvc.perform(MockMvcRequestBuilders.get("/users/me")
            .accept(MediaType.APPLICATION_JSON))
                
                // expect
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.id").value(Integer.toString(testUser.getId())))
                .andExpect(jsonPath("$.username").value(testUser.getName()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()));
    }
    
    @Test
    public void testRegisterUser() throws Exception {
        // given
        given(userService.registerNewUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .willReturn(testUser);
        AccountDTO account = new AccountDTO();
        account.setEmail(testUser.getName());
        account.setUsername(testUser.getEmail());
        account.setPassword("fakepassword");
        
        Gson gson = new Gson();
        String json = gson.toJson(account);
        
        // when
        mvc.perform(MockMvcRequestBuilders.post("/users/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                
                
                // expect
                .andExpect(status().isCreated());
    }
    
    
}
