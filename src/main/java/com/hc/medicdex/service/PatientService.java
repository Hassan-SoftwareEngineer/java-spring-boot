package com.hc.medicdex.service;

import com.hc.medicdex.dto.PatientDto;
import com.hc.medicdex.dto.UserEntityDto;
import com.hc.medicdex.entity.HospitalEntity;
import com.hc.medicdex.entity.PatientEntity;
import com.hc.medicdex.entity.UserEntity;
import com.hc.medicdex.mapper.PatientMapper;
import com.hc.medicdex.repository.HospitalRepository;
import com.hc.medicdex.repository.PatientRepository;
import com.hc.medicdex.util.DateFormattingUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class PatientService {
    private final PatientRepository repository;
    private final PatientMapper patientMapper;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private StaffService staffService;


    public PatientDto save(PatientDto patientDto, String username) {
        DateFormattingUtil dateFormattingUtil= new DateFormattingUtil();
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        PatientEntity patient = patientMapper.toEntity(patientDto);
        try {
            if(patient.getPatientType().equals("Indoor") || patient.getPatientType().equals("Indoor") ||
                    patient.getPatientType().equals("ipd") || patient.getPatientType().equals("IPD")){
               patient.setAdmittedOn(dateFormattingUtil.getDateFromString(patientDto.getAdmittedOn()));
                patient.setDischargedOn(patientDto.getDischargedOn()!=null?dateFormattingUtil.getDateFromString(patientDto.getDischargedOn()):null);
            }
            patient.setCreatedOn(dateFormattingUtil.getCurrentDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        patient.setHospital(hospitalRepository.findByUser(user).get());

        return patientMapper.toDto(repository.save(patient));
    }
    public PatientDto saveUpdate(PatientEntity patient, PatientDto patientDto) throws ParseException {
        DateFormattingUtil dateFormattingUtil= new DateFormattingUtil();
        patient.setPatientName(patientDto.getPatientName());
        patient.setFatherName(patientDto.getFatherName());
        patient.setCnic(patientDto.getCnic());
        patient.setAge(patientDto.getAge());
        patient.setPatientType(patientDto.getPatientType());
        patient.setGender(patientDto.getGender());
        if(patientDto.getDischargedOn() != null && patientDto.getDischargedOn() != ""){
            patient.setDischargedOn(dateFormattingUtil.getDateFromString(patientDto.getDischargedOn()));
        }
        if(patientDto.getAdmittedOn() != null && patientDto.getAdmittedOn() != ""){
            patient.setAdmittedOn(dateFormattingUtil.getDateFromString(patientDto.getAdmittedOn()));
        }
        patient.setStaffEntities(staffService.dtoTOEntityList(patientDto.getStaffEntities()));
        repository.save(patient);
        return patientMapper.toDto(patient);
    }

    public void deleteById(Integer id) {
        Optional<PatientEntity> patient = repository.findById(id);
        if(patient.get().getStaffEntities() != null){
            patient.get().getStaffEntities().removeAll(patient.get().getStaffEntities());
        }
        repository.delete(patient.get());
    }

    public PatientDto findById(Integer id) {
        return patientMapper.toDto(repository.findById(id).get());
    }

    public Page<PatientDto> findByCondition(PatientDto patientDto, Pageable pageable) {
        Page<PatientEntity> entityPage = repository.findAll(pageable);
        List<PatientEntity> entities = entityPage.getContent();
        return new PageImpl<>(patientMapper.toDto(entities), pageable, entityPage.getTotalElements());
    }

    public PatientDto update(PatientDto patientDto, Integer id, String username) throws ParseException {
        //PatientDto data = findById(id);
        Optional<PatientEntity> patient = repository.findById(id);

        //repository.findById(id);

        //PatientEntity entity = patientMapper.toEntity(patientDto);
        //BeanUtil.copyProperties(data, entity);
        return saveUpdate(patient.get(),patientDto);
    }

    public Map<String,Object> findAllPatientByType(int page, int size , String patientType, String username){
        Map<String,Object> result = new HashMap<>();
        List<PatientDto> patientDtoList = new ArrayList<>();
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        HospitalEntity hospital = hospitalRepository.findByUser(user).get();
        Page<PatientEntity> patientEntities = repository.findByPatientTypeAndHospital(PageRequest.of(page-1, size), patientType, hospital);
        patientDtoList = patientMapper.toDto(patientEntities.getContent());
        result.put("Patients", patientDtoList);
        result.put("currentPage", patientEntities.getNumber());
        result.put("totalItems", patientEntities.getTotalElements());
        result.put("totalPages", patientEntities.getTotalPages());
        result.put("size", patientEntities.getSize());
        return result;
    }
    public Map<String,Object> findAllPatients(int page, int size,String searchQuery ,String username){
        Map<String,Object> result = new HashMap<>();
        Page<PatientEntity> patientEntityList;
        List<PatientDto> patientDtos = new ArrayList<>();
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        Optional<HospitalEntity> hospital = hospitalRepository.findByUser(user);
        if(searchQuery!=null && !searchQuery.isEmpty()){
            patientEntityList = repository.searchByHospital(PageRequest.of(page-1, size),searchQuery,hospital.get());
        }else {
            patientEntityList = repository.findAllByHospital(PageRequest.of(page-1, size),hospital.get());
        }
        //patientEntityList = repository.findAllByHospital(PageRequest.of(page-1, size),hospital.get());
        patientDtos = patientMapper.toDto(patientEntityList.getContent());
        result.put("patients",patientDtos);
        result.put("opd",patientDtos.stream().filter(p->p.getPatientType().equals("OPD") ||
                        p.getPatientType().equals("opd"))
                .count());
        result.put("ipd",patientDtos.stream().filter(p->p.getPatientType().equals("IPD") ||
                        p.getPatientType().equals("ipd"))
                .count());
        result.put("currentPage", patientEntityList.getNumber());
        result.put("totalItems", patientEntityList.getTotalElements());
        result.put("totalPages", patientEntityList.getTotalPages());
        result.put("size", patientEntityList.getSize());
        return result;
    }
    public Map<String,Object> findAllPatientsByDateRange(String patientType,String startDate, String endDate,String username) throws ParseException {
        DateFormattingUtil dateFormattingUtil= new DateFormattingUtil();
        Map<String,Object> result = new HashMap<>();
        List<PatientEntity> patientEntityList;
        List<PatientDto> patientDtoList = new ArrayList<>();
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        Optional<HospitalEntity> hospital = hospitalRepository.findByUser(user);
        patientEntityList = repository.findByHospitalAndCreatedOnBetweenAndPatientType(hospital.get(),dateFormattingUtil.getDateFromString(startDate),
                dateFormattingUtil.getDateFromString(endDate),patientType);
        patientDtoList = patientMapper.toDto(patientEntityList);
        result.put("patients", patientDtoList);
        return result;
    }
    public Map<String,Object> findAllPatientsByToday(int page, int size, String username) throws ParseException {
        DateFormattingUtil dateFormattingUtil= new DateFormattingUtil();
        Map<String,Object> result = new HashMap<>();
        Page<PatientEntity> patientEntityList;
        List<PatientDto> patientDtoList = new ArrayList<>();
        UserEntity user = userManagerService.getAuthenticatedUser(username);
        Optional<HospitalEntity> hospital = hospitalRepository.findByUser(user);
        patientEntityList = repository.findByHospitalAndCreatedOn(PageRequest.of(page-1, size),hospital.get(),dateFormattingUtil.getCurrentDate());
        patientDtoList = patientMapper.toDto(patientEntityList.getContent());
        result.put("opd",patientDtoList.stream().filter(p->p.getPatientType().equals("OPD") ||
                        p.getPatientType().equals("opd"))
                .count());
        result.put("ipd",patientDtoList.stream().filter(p->p.getPatientType().equals("IPD") ||
                        p.getPatientType().equals("ipd"))
                .count());

        result.put("patients",patientDtoList);
        result.put("currentPage", patientEntityList.getNumber());
        result.put("totalItems", patientEntityList.getTotalElements());
        result.put("totalPages", patientEntityList.getTotalPages());
        result.put("size", patientEntityList.getSize());
        return result;
    }
}