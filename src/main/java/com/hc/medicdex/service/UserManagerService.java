package com.hc.medicdex.service;

import com.hc.medicdex.dto.UserEntityDto;
import com.hc.medicdex.entity.UserEntity;
import com.hc.medicdex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserManagerService implements UserDetailsManager {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void createUser(UserDetails user) {

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String hospitalId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByHospitalIdOrEmail(hospitalId,hospitalId);
        if(userEntity != null){
            return entityToDto(userEntity);
        }else{
            throw new UsernameNotFoundException(hospitalId + "not found");
        }
    }
    public UserEntityDto loadUserByUsername(String hospitalId,String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByHospitalIdOrEmail(hospitalId,hospitalId);
        if(userEntity != null){
            return entityToDto(userEntity);
        }else{
            throw new UsernameNotFoundException(hospitalId + "not found");
        }
    }
    public UserEntityDto entityToDto(UserEntity user){
        UserEntityDto userEntityDto = UserEntityDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .password(user.getPassword())
                .authority(user.getAuthority())
                .email(user.getEmail())
                .address(user.getAddress())
                .hospitalId(user.getHospitalId())
                .build();
        return userEntityDto;
    }
    public UserEntity dtoToEntity(UserEntityDto user){
        UserEntity userEntity = UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .password(user.getPassword())
                .authority(user.getAuthority())
                .email(user.getEmail())
                .address(user.getAddress())
                .hospitalId(user.getHospitalId())
                .build();
        return userEntity;
    }
    public UserEntity getAuthenticatedUser(String username){
//        String username = null;
//        Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal). getUsername();
//        } else {
//            username = principal.toString();
//        }
        UserEntityDto userDto = loadUserByUsername(username,username);
        UserEntity user = dtoToEntity(userDto);
        return user;
    }
}
