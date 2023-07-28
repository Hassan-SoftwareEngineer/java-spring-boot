package com.hc.medicdex.service;

import com.hc.medicdex.dto.HospitalEntityDto;
import com.hc.medicdex.dto.StaffEntityDto;
import com.hc.medicdex.dto.UserEntityDto;
import com.hc.medicdex.entity.HospitalEntity;
import com.hc.medicdex.entity.StaffEntity;
import com.hc.medicdex.entity.UserEntity;
import com.hc.medicdex.repository.HospitalRepository;
import com.hc.medicdex.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.security.PrivateKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private UserManagerService userManagerService;
    public void createStaff(StaffEntityDto staffEntityDto, String username){
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        StaffEntity staff = dtoTOEntity(staffEntityDto);
        staff.setHospital(hospitalRepository.findByUser(user).get());
        staffRepository.save(staff);
    }
    public Map<String,Object> getAllStaff(int page, int size, String searchQuery, String username){
        Page<StaffEntity> staffEntityList = null;
        Map<String,Object> result = new HashMap<>();
        List<StaffEntityDto> StaffEntityDtoList = new ArrayList<>();
        StaffEntityDto staffEntityDto = null;
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        HospitalEntity hospital = hospitalRepository.findByUser(user).get();
        if(searchQuery!=null && !searchQuery.isEmpty()){
            staffEntityList = staffRepository.searchByHospital(PageRequest.of(page-1, size),searchQuery,hospital);
        }else {
            staffEntityList = staffRepository.findAllByHospital(PageRequest.of(page-1, size),hospital);
        }
        //Page<StaffEntity> staffEntityList =  staffRepository.findAllByHospital(PageRequest.of(page-1, size),hospital);
        for(StaffEntity staff: staffEntityList.getContent()){
            staffEntityDto = entityToDto(staff);
            StaffEntityDtoList.add(staffEntityDto);
        }
        result.put("staff", StaffEntityDtoList);
        result.put("currentPage", staffEntityList.getNumber());
        result.put("totalItems", staffEntityList.getTotalElements());
        result.put("totalPages", staffEntityList.getTotalPages());
        result.put("size", staffEntityList.getSize());
        result.put("total_doctors",StaffEntityDtoList.stream().filter(s -> s.getStaffType().equals("doctor") ||
                s.getStaffType().equals("Doctor")).count());
        result.put("total_nurses",StaffEntityDtoList.stream().filter(s -> s.getStaffType().equals("nurse") ||
                s.getStaffType().equals("Nurse")).count());
        return result;
    }
    public void updateStaffById(StaffEntityDto staffEntityDto, Integer id){
        Optional<StaffEntity> staffEntity = staffRepository.findById(id);
        StaffEntity staff = staffEntity.get();
        staff.setStaffName(staffEntityDto.getStaffName());
        staff.setStaffType(staffEntityDto.getStaffType());
        staff.setFee(staffEntityDto.getFee());
        staff.setPhone(staffEntityDto.getPhone());
        staff.setTiming(staffEntityDto.getTiming());
        staff.setSpecialization(staffEntityDto.getSpecialization());
        staffRepository.save(staff);
    }
    public void deleteStaffById(Integer id){
        staffRepository.deleteById(id);
    }
    public StaffEntity dtoTOEntity(StaffEntityDto staffEntityDto){
        StaffEntity staff = StaffEntity.builder()
                .staffName(staffEntityDto.getStaffName())
                .staffType(staffEntityDto.getStaffType())
                .fee(staffEntityDto.getFee())
                .specialization(staffEntityDto.getSpecialization())
                .staffType(staffEntityDto.getStaffType())
                .phone(staffEntityDto.getPhone())
                .timing(staffEntityDto.getTiming())
                .build();
        return staff;
    }
    public List<StaffEntity> dtoTOEntityList(List<StaffEntityDto> staffDto){
        List<StaffEntity> staffEntities = new ArrayList<>();
        for (StaffEntityDto staffEntityDto: staffDto) {
            Optional<StaffEntity> staff = staffRepository.findById(staffEntityDto.getId());
            staffEntities.add(staff.get());
        }

        return staffEntities;
    }
    public StaffEntityDto entityToDto(StaffEntity staff){
        StaffEntityDto staffEntityDto = StaffEntityDto.builder()
                .id(staff.getId())
                .staffName(staff.getStaffName())
                .specialization(staff.getSpecialization())
                .fee(staff.getFee())
                .staffType(staff.getStaffType())
                .phone(staff.getPhone())
                .timing(staff.getTiming())
                .hospital_id(staff.getHospital() != null?staff.getHospital().getId():-1)
                .build();
        return staffEntityDto;
    }
    public List<StaffEntityDto> entityToDtoList(List<StaffEntity> staffEntities){
        List<StaffEntityDto> stafDtos = new ArrayList<>();
        for (StaffEntity staff: staffEntities) {
            StaffEntityDto staffEntity = StaffEntityDto.builder()
                    .id(staff.getId())
                    .staffName(staff.getStaffName())
                    .specialization(staff.getSpecialization())
                    .fee(staff.getFee())
                    .staffType(staff.getStaffType())
                    .phone(staff.getPhone())
                    .timing(staff.getTiming())
                    .hospital_id(staff.getHospital() != null?staff.getHospital().getId():-1)
                    .build();
            stafDtos.add(staffEntity);
        }

        return stafDtos;
    }

    public Map<String,Object> getAllDoctor(String username) {
        List<StaffEntity> staffEntityList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        List<StaffEntityDto> StaffEntityDtoList = new ArrayList<>();
        StaffEntityDto staffEntityDto = null;
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        HospitalEntity hospital = hospitalRepository.findByUser(user).get();
        staffEntityList = staffRepository.findAllByHospital(hospital).stream().filter(s -> s.getStaffType().equals("Doctor")).collect(Collectors.toList());
        for(StaffEntity staff: staffEntityList){
            staffEntityDto = entityToDto(staff);
            StaffEntityDtoList.add(staffEntityDto);
        }
        result.put("doctors",StaffEntityDtoList);
        return result;
    }
}
