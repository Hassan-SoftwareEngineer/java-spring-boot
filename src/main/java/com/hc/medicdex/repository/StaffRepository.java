package com.hc.medicdex.repository;

import com.hc.medicdex.entity.HospitalEntity;
import com.hc.medicdex.entity.StaffEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StaffRepository extends JpaRepository<StaffEntity, Integer> {
    Page<StaffEntity> findAllByHospital(PageRequest pageable, HospitalEntity hospital);
    List<StaffEntity> findAllByHospital(HospitalEntity hospital);
    @Query("SELECT s FROM StaffEntity s "
            + "WHERE s.hospital = :hospital AND (s.staffType LIKE %:keyword%"
            + " OR s.specialization LIKE %:keyword%"
            + " OR s.staffName LIKE %:keyword%"
            + " OR CONCAT(s.fee, '') LIKE %:keyword% )")
    Page<StaffEntity> searchByHospital(PageRequest pageable,@Param("keyword") String keyword,@Param("hospital") HospitalEntity hospital);
}