package com.hc.medicdex.repository;

import com.hc.medicdex.entity.HospitalEntity;
import com.hc.medicdex.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HospitalRepository extends JpaRepository<HospitalEntity, Integer>, JpaSpecificationExecutor<HospitalEntity> {
    Optional<HospitalEntity> findByUser(UserEntity user);

}