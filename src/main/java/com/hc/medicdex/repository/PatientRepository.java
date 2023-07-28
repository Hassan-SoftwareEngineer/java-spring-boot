package com.hc.medicdex.repository;

import com.hc.medicdex.entity.HospitalEntity;
import com.hc.medicdex.entity.PatientEntity;
import com.hc.medicdex.entity.StaffEntity;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientEntity, Integer>, JpaSpecificationExecutor<PatientEntity> {
    Page<PatientEntity> findByPatientTypeAndHospital(PageRequest pageable, String patientType, HospitalEntity hospital);
    Page<PatientEntity> findAllByHospital(PageRequest pageable, HospitalEntity hospital);
    List<PatientEntity> findByHospitalAndCreatedOnBetweenAndPatientType(HospitalEntity hospital,Date startdate, Date enddate, String patientType);
    Page<PatientEntity> findByHospitalAndCreatedOn(PageRequest pageable,HospitalEntity hospital,Date today);

    @Query("SELECT p FROM PatientEntity p "
            + "WHERE p.hospital = :hospital AND (p.cnic LIKE %:keyword%"
            + " OR p.gender LIKE %:keyword%"
            + " OR p.fatherName LIKE %:keyword%"
            + " OR CONCAT(p.age, '') LIKE %:keyword% )")
    Page<PatientEntity> searchByHospital(PageRequest pageable, @Param("keyword") String keyword, @Param("hospital") HospitalEntity hospital);
}