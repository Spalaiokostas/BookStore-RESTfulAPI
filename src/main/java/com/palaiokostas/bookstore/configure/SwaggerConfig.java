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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author Spyros Palaiokostas
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    @Value("${bookstore.oauth.clientId}")
    String clientId;
    @Value("${bookstore.oauth.clientSecret}")
    String clientSecret;
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.palaiokostas.bookstore.controller"))              
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext()));
    }
    
    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scopeSeparator(" ")
                .build();
    }
    
    
    private SecurityScheme securityScheme() {
        
        List<AuthorizationScope> authorizationScopeList = new ArrayList();
        authorizationScopeList.add(new AuthorizationScope("read", "for read operations"));
        authorizationScopeList.add(new AuthorizationScope("write", "for write operations"));

        List<GrantType> grantTypes = new ArrayList();
        GrantType creGrant = new ResourceOwnerPasswordCredentialsGrant("http://localhost:8080/oauth/token");
 
        grantTypes.add(creGrant);
        
        return new OAuth("oauth2schema", authorizationScopeList, grantTypes);
    }
    
    
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.ant("/users/**"))
                .forPaths(PathSelectors.ant("/orders/**"))
                .build();
    }
    
    
    private List<SecurityReference> defaultAuth() {

        AuthorizationScope[] scopes = { 
        new AuthorizationScope("read", "for read operations"), 
        new AuthorizationScope("write", "for write operations")};

        return Collections.singletonList(new SecurityReference("oauth2schema", scopes));
    }
}
