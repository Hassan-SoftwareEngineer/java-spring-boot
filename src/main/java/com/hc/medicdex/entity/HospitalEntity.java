package com.hc.medicdex.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Entity
@Table(name = "hospital_entity")
public class HospitalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "name", nullable = false)
    private String hospitalName;
    @Column(name = "ownerName", nullable = false)
    private String ownerName;
    @Column(name = "address")
    private String hospitalAddress;
    @Column(name = "landLine_PTCL")
    private String landLine_PTCL;
    @Column(name = "m_phone")
    private String mobilePhone;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "hospital")
    private List<StaffEntity> staff = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<PatientEntity> patients = new ArrayList<>();
}