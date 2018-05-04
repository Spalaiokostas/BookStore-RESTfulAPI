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
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.palaiokostas.bookstore.commons.dataobjects.OrderDTOs.OrderBookDTO;
import com.palaiokostas.bookstore.commons.dataobjects.OrderDTOs.OrderDTO;
import com.palaiokostas.bookstore.model.dataTypes.OrderStatus;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
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
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		DbUnitTestExecutionListener.class })

public class OrderControllerIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    private OAuth2RestTemplate oauthRestTemplate;
    
    @Before
    public void setUp() {
        
        ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
        resourceDetails.setUsername("testUser");
        resourceDetails.setPassword("root");
        resourceDetails.setAccessTokenUri(format("http://localhost:%d/oauth/token", port));
        resourceDetails.setClientId("my-trusted-client");
        resourceDetails.setClientSecret("secret");
        resourceDetails.setGrantType("password");
        resourceDetails.setScope(asList("read"));

        DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

        oauthRestTemplate = new OAuth2RestTemplate(resourceDetails, clientContext);
        List<HttpMessageConverter<?>> mc = oauthRestTemplate.getMessageConverters();
        mc.add(new MappingJackson2HttpMessageConverter());
        oauthRestTemplate.setMessageConverters(mc);
        
    }
    
    @Test
    @DatabaseSetup("classpath:dbUnit/testData.xml")
    public void testListUserOrders() {
        // execute
        ResponseEntity<List<OrderDTO>> response = oauthRestTemplate.exchange(
                format("http://localhost:%d/orders", port),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<OrderDTO>>() {});
         
         // collect response
        HttpStatus status = response.getStatusCode();
        List<OrderDTO> orders = response.getBody();
        
        // verify response
        assertThat(status, equalTo(HttpStatus.OK));
        assertNotNull(orders);
        assertThat(orders.size(), is(equalTo(1)));
        
        // verify order
        OrderDTO order = orders.get(0);
        assertThat(order.getBookID(), is(equalTo(10)));
        assertThat(order.getUserID(), is(equalTo(10)));
        assertThat(order.getQuantity(), is(equalTo(1)));
        assertThat(order.getStatus(), is(OrderStatus.UNPROCESSED.name()));     
    }
    
    
    @Test
    @DatabaseSetup("classpath:dbUnit/testData.xml")
    @ExpectedDatabase(value ="classpath:dbUnit/createdOrder.xml", 
            table = "orders",
            assertionMode=DatabaseAssertionMode.NON_STRICT)
    //@DatabaseTearDown(value = "classpath:dbUnit/testData.xml")
    public void purchaseBook() {
        // prepare
        OrderBookDTO order = new OrderBookDTO();
        order.setBookId(10);
        order.setQuantity(2);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<OrderBookDTO> entity = new HttpEntity<>(order, headers);
        
        // execute
        ResponseEntity<Void> response = oauthRestTemplate.exchange(
                format("http://localhost:%d/orders", port),
                HttpMethod.POST,
                entity,
                Void.class);
        
        // validate
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertTrue(response.getHeaders().containsKey("Location"));
        
    }
    
    
    
}
