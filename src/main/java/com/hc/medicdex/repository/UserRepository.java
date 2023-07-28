package com.hc.medicdex.repository;

import com.hc.medicdex.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    UserEntity findByHospitalIdOrEmail(String hospitalId, String email);

    Boolean existsByEmail(String email);
}
