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

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.palaiokostas.bookstore.commons.dataobjects.UserDTOs.AccountDTO;
import com.palaiokostas.bookstore.commons.dataobjects.UserDTOs.UserDTO;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 *
 * @author Spyros Palaiokostas
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test-application.properties")
@TestExecutionListeners({   DependencyInjectionTestExecutionListener.class,
                            DbUnitTestExecutionListener.class })
public class UserControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    AuthorizationServerTokenServices tokenservice;
  
    @LocalServerPort
    private int port;
    
    @Test
    @DatabaseSetup(value = "classpath:dbUnit/testData.xml")
    public void testGetUserProfile() {
        
        // prepare
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("testUser");
        resourceDetails.setPassword("root");
        resourceDetails.setAccessTokenUri(format("http://localhost:%d/oauth/token", port));
        resourceDetails.setClientId("my-trusted-client");
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(asList("read"));

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        OAuth2RestTemplate oauthRestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        List<HttpMessageConverter<?>> mc = oauthRestTemplate.getMessageConverters();
        mc.add(new MappingJackson2HttpMessageConverter());
        oauthRestTemplate.setMessageConverters(mc);
        
        // execute
        final UserDTO user = oauthRestTemplate.getForObject(
                format("http://localhost:%d/users/me", port), 
                UserDTO.class);
        
        // verify
        assertNotNull(user);
        assertThat(user.getEmail(), is(equalTo("fakeEmail@provider.domain")));
        assertThat(user.getUsername(), is(equalTo("testUser")));
    }
    
    @Test
    @DatabaseSetup(value ="classpath:dbUnit/empty.xml")
    @ExpectedDatabase(value ="classpath:dbUnit/createdUser.xml", 
            table = "users",
            assertionMode=DatabaseAssertionMode.NON_STRICT)
    public void testRegisterUser() {
        // prepeare
        AccountDTO account = new AccountDTO();
        account.setEmail("unique@email.com");
        account.setUsername("uniqueUsername");
        account.setPassword("qwerty");
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountDTO> entity = new HttpEntity<>(account, headers);
        
        // excecute
        ResponseEntity<Map> response = restTemplate.exchange(
                "/users/register",
                HttpMethod.POST,
                entity,
                Map.class);
        
        // validate
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody(), nullValue());
    }

    
    
}
