package com.hc.medicdex.controller;

import com.hc.medicdex.dto.HospitalEntityDto;
import com.hc.medicdex.repository.HospitalRepository;
import com.hc.medicdex.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllHospital(@RequestParam int page, @RequestParam int size){
        Map<String,Object> hospitalList = hospitalService.getAllHospitals(page, size);
        return new ResponseEntity<>(hospitalList, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createHospital(@RequestBody HospitalEntityDto hospitalEntityDto,@RequestParam String username){
        Map<String,Object> res= hospitalService.createHospital(hospitalEntityDto, username);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public Integer UpdateHospital(@RequestBody HospitalEntityDto hospitalEntityDto,@PathVariable Integer id){
        hospitalService.updateHospitalById(hospitalEntityDto,id);
        return id;
    }

}
