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
package com.palaiokostas.bookstore.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 *
 * @author Spyros Palaiokostas
 */

@Configuration
@EnableAuthorizationServer()
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    
    @Value("${bookstore.oauth.clientId}")
    String clientId;
    @Value("${bookstore.oauth.clientSecret}")
    String clientSecret;
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authManager;
    
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpointConfigurer) {
        endpointConfigurer.tokenStore(tokenStore())
            .authenticationManager(authManager);
    }
    
    @Override
    public void configure(ClientDetailsServiceConfigurer clientConfigurer) throws Exception {
        String secretEncoded = passwordEncoder.encode(clientSecret);
        clientConfigurer.inMemory()
                .withClient(clientId).secret(secretEncoded)
                .authorizedGrantTypes("password")
                //.authorities("ROLE_TRUSTED_CLIENT")
                .scopes("read", "write");
        
    }
    
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
                
    }
    
    @Bean
    public TokenStore tokenStore() {
	return new InMemoryTokenStore();
    }
   
    
    
    
    
}
