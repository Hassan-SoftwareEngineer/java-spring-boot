package com.hc.medicdex.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "staff_entity")
public class StaffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String staffName;
    private String specialization;
    private Integer fee;
    private String staffType;
    private String phone;
    private String timing;
    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private HospitalEntity hospital;
    @ManyToMany(mappedBy = "staffEntities")
    private List<PatientEntity> patientEntities = new ArrayList<>();

}