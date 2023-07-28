package com.hc.medicdex.controller;

import com.hc.medicdex.dto.StaffEntityDto;
import com.hc.medicdex.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam int page, @RequestParam int size, @RequestParam String searchQuery,
                                    @RequestParam String username){
        Map<String,Object> staffEntityDto = staffService.getAllStaff(page, size, searchQuery,username);
        return new ResponseEntity<>(staffEntityDto, HttpStatus.OK);
    }
    @PostMapping("/create")
    public String createStaff(@RequestBody StaffEntityDto staffEntityDto, @RequestParam String username){
        staffService.createStaff(staffEntityDto, username);
        return "created";
    }
    @PutMapping("/update/{id}")
    public String updateStaff(@RequestBody StaffEntityDto staffEntityDto, @PathVariable Integer id){
        staffService.updateStaffById(staffEntityDto,id);
        return "created";
    }
    @DeleteMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Integer id){
        staffService.deleteStaffById(id);
        return "Deleted";
    }

    @GetMapping("/all/doctor")
    public ResponseEntity<?> getAllDoctor(@RequestParam String username){
        Map<String,Object> staffEntityDto = staffService.getAllDoctor(username);
        return new ResponseEntity<>(staffEntityDto, HttpStatus.OK);
    }

}
