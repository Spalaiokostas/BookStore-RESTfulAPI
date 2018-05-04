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
package com.palaiokostas.bookstore.security;

import com.palaiokostas.bookstore.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Spyros Palaiokostas
 */
public interface AuthenticationFacade {
    
    Authentication getAuthentication();
    
    User authenticatedUser();
    
}

@Component
class AuthenticationFacadeImp implements AuthenticationFacade {
    
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User authenticatedUser() {
        MyUserDetails userDetails = (MyUserDetails) getAuthentication().getPrincipal();
        return userDetails.getUser();
    }
    
    
    
}
