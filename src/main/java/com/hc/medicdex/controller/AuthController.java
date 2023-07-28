package com.hc.medicdex.controller;

import com.hc.medicdex.authentication.CustomAuthenticationProvider;
import com.hc.medicdex.dto.UserEntityDto;
import com.hc.medicdex.dto.UserLoginDto;
import com.hc.medicdex.repository.UserRepository;
import com.hc.medicdex.service.UserManagerService;
import com.hc.medicdex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/user")
public class AuthController {
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private UserManagerService userManagerService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody UserLoginDto userLoginDto){
        //UserDetails userDetails = userManagerService.loadUserByUsername(userLoginDto.getUserName());
        Map<String,Object> response = userService.authenticateUser(userLoginDto);
//        Authentication authentication = customAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(),userLoginDto.getPassword()));
//        if(authentication.isAuthenticated()){
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            return new ResponseEntity<>(SecurityContextHolder.getContext(), HttpStatus.OK);
//        }else{
//            return new ResponseEntity<>("User name, Email or password is incorrect", HttpStatus.BAD_REQUEST);
//        }
        if(response != null){
            return new ResponseEntity<>(response,HttpStatus.OK);
        }else{
            return new ResponseEntity<>("User name, Email or password is incorrect", HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/logout")
    public String userLogout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "logout successfully";
    }
    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserEntityDto userEntityDto){
//        if(userRepository.existsByUsername(userEntityDto.getUsername())){
//            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
//        }
        if(userRepository.existsByEmail(userEntityDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        userService.createUser(userEntityDto);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
    @PutMapping("/update")
    public String updateUser(){
        return " updated succefully";
    }
    @DeleteMapping("/delete")
    public String deleteUser(){
        return "deleted successfully";
    }
}
