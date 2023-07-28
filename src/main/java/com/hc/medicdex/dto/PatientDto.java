package com.hc.medicdex.dto;

//import com.hc.medicdex.StaffEntityDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link com.hc.medicdex.entity.PatientEntity} entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto implements Serializable {
    private Integer id;
    private String patientName;
    private String fatherName;
    private String cnic;
    private int age;
    private String patientType;
    private String gender;
    private String mrNumber;
    private String diagnosis;
    private String procedure;
    private String remarks;
    private String admittedOn;
    private String dischargedOn;
    private List<StaffEntityDto> staffEntities = new ArrayList<>();

}