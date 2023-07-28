package com.hc.medicdex.authentication;

import com.hc.medicdex.dto.UserEntityDto;
import com.hc.medicdex.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserManagerService userManagerService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());
        UserEntityDto userDetails = userManagerService.loadUserByUsername(username,password);
        PasswordEncoder p = new BCryptPasswordEncoder();
        if((username.equals(userDetails.getUsername()) || username.equals(userDetails.getEmail())) && p.matches(password,userDetails.getPassword())){
            return new UsernamePasswordAuthenticationToken(username,password, userDetails.getAuthorities());
        }else{
            authentication.setAuthenticated(false);
            return authentication;
            //throw new AuthenticationCredentialsNotFoundException("Error in authentication");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
