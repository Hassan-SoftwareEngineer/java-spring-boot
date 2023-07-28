package com.hc.medicdex.service;

import com.hc.medicdex.authentication.CustomAuthenticationProvider;
import com.hc.medicdex.dto.UserEntityDto;
import com.hc.medicdex.dto.UserLoginDto;
import com.hc.medicdex.entity.UserEntity;
import com.hc.medicdex.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HospitalService hospitalService;

    public Map<String,Object> authenticateUser(UserLoginDto userLoginDto){
        Map<String,Object> response = new HashMap<>();
        Authentication authentication = customAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUserName(),userLoginDto.getPassword()));
        if(authentication.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            response.put("user",SecurityContextHolder.getContext());
            if(hospitalService.isHospitalExistWithUser(authentication.getName())){
                response.put("hospital",hospitalService.getHospitalNameByUser(authentication.getName()));
            }else {
                response.put("hospital",null);
            }
            //response.get(hospitalService.getHospitalNameByUser(authentication.getName()));
            return response;
        }else{
            return null;
        }
    }
    public void createUser(UserEntityDto userEntityDto){
        UserEntity user = UserEntity.builder()
                .hospitalId(userEntityDto.getHospitalId())
                .email(userEntityDto.getEmail())
                .password(passwordEncoder.encode(userEntityDto.getPassword()))
                .authority("admin")
                .phone(userEntityDto.getPhone())
                .firstName(userEntityDto.getFirstName())
                .lastName(userEntityDto.getLastName())
                .address(userEntityDto.getAddress())
                .build();

        userRepository.save(user);
        log.info("User is saved");
    }
}
