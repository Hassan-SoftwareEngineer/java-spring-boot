package com.hc.medicdex.service;

import com.hc.medicdex.dto.HospitalEntityDto;
import com.hc.medicdex.dto.UserEntityDto;
import com.hc.medicdex.entity.HospitalEntity;
import com.hc.medicdex.entity.UserEntity;
import com.hc.medicdex.repository.HospitalRepository;
import com.hc.medicdex.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.*;

@Service
public class HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserManagerService userManagerService;
    public Map<String,Object> createHospital(HospitalEntityDto hospitalEntityDto, String username){
        Map<String,Object> response = new HashMap<>();
        //String username =null;
//        Object principal = SecurityContextHolder. getContext(). getAuthentication(). getPrincipal();
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal). getUsername();
//        } else {
//            username = principal.toString();
//        }
        UserEntityDto userDto = userManagerService.loadUserByUsername(username,username);
        UserEntity user = userManagerService.dtoToEntity(userDto);
        System.out.println(hospitalRepository.findByUser(user).isPresent());
        if(!hospitalRepository.findByUser(user).isPresent()){
            HospitalEntity hospital = dtoToEntity(hospitalEntityDto);
            hospital.setUser(user);
            hospitalRepository.save(hospital);
            response.put("status",HttpStatus.CREATED.value());
            response.put("created",true);
        }else {
            response.put("status", HttpStatus.BAD_REQUEST);
            response.put("created",false);
            response.put("error","Hospital already exist with this user");
        }
        return response;
    }
    public Map<String,Object> getAllHospitals(int page, int size){
        Map<String,Object> result = new HashMap<>();
        List<HospitalEntityDto> hospitalEntityDtoList = new ArrayList<>();
        HospitalEntityDto hospitalEntityDto = null;

        //Pageable pageable = (Pageable) PageRequest.of(page,size);
        Page<HospitalEntity> hospitalPaging= hospitalRepository.findAll(PageRequest.of(page-1,size));
        //List<HospitalEntity>  hospitalEntityList = hospitalRepository.findAll();
        for(HospitalEntity hospital: hospitalPaging.getContent()){
            hospitalEntityDto = entityToDto(hospital);
            hospitalEntityDtoList.add(hospitalEntityDto);
        }
        result.put("hospitals", hospitalEntityDtoList);
        result.put("currentPage", hospitalPaging.getNumber());
        result.put("totalItems", hospitalPaging.getTotalElements());
        result.put("totalPages", hospitalPaging.getTotalPages());
        result.put("size", hospitalPaging.getSize());

        return result;
    }
    public void updateHospitalById(HospitalEntityDto hospitalEntityDto, Integer id){
        Optional<HospitalEntity> hospitalEntity = hospitalRepository.findById(id);
        HospitalEntity hospital = hospitalEntity.get();
        hospital.setHospitalName(hospitalEntityDto.getHospitalName());
        hospital.setHospitalAddress(hospitalEntityDto.getHospitalAddress());
        hospital.setOwnerName(hospitalEntityDto.getOwnerName());
        hospital.setMobilePhone(hospitalEntityDto.getMobilePhone());
        hospital.setLandLine_PTCL(hospitalEntityDto.getLandLine_PTCL());

        hospitalRepository.save(hospital);
    }

    public HospitalEntity dtoToEntity(HospitalEntityDto hospitalEntityDto){
        HospitalEntity hospital = HospitalEntity.builder()
                .hospitalName(hospitalEntityDto.getHospitalName())
                .ownerName(hospitalEntityDto.getOwnerName())
                .hospitalAddress(hospitalEntityDto.getHospitalAddress())
                .landLine_PTCL(hospitalEntityDto.getLandLine_PTCL())
                .mobilePhone(hospitalEntityDto.getMobilePhone())
                .build();
        return hospital;
    }
    public HospitalEntityDto entityToDto(HospitalEntity hospital){
        HospitalEntityDto hospitalEntityDto = HospitalEntityDto.builder()
                .id(hospital.getId())
                .hospitalName(hospital.getHospitalName())
                .ownerName(hospital.getOwnerName())
                .hospitalAddress(hospital.getHospitalAddress())
                .landLine_PTCL(hospital.getLandLine_PTCL())
                .mobilePhone(hospital.getMobilePhone())
                .user_id(hospital.getUser() != null ?hospital.getUser().getId():0)
                .build();
        return hospitalEntityDto;
    }
    public String getHospitalNameByUser(String username){
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        return hospitalRepository.findByUser(user).get().getHospitalName();
    }
    public boolean isHospitalExistWithUser(String username){
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        if(hospitalRepository.findByUser(user).isPresent()){
            return true;
        }else{
            return false;
        }
    }
}

