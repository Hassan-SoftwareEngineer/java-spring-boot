package com.hc.medicdex.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "patient_entity")
public class PatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String patientName;
    private String fatherName;
    //private String husbandName;
    private String cnic;
    private int age;
    private String patientType;
    private String gender;
    private String mrNumber;

    private String diagnosis;

    private String procedure;

    private String remarks;
    @Temporal(TemporalType.DATE)
    private Date admittedOn;
    @Temporal(TemporalType.DATE)
    private Date dischargedOn;
    @Temporal(TemporalType.DATE)
    private Date createdOn;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "doctor_patient",
            joinColumns = { @JoinColumn(name = "patient_id") },
            inverseJoinColumns = { @JoinColumn(name = "doctor_id" , referencedColumnName = "id") })
    private List<StaffEntity> staffEntities = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private HospitalEntity hospital;

}