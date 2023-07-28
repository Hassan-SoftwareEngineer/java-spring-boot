package com.hc.medicdex.mapper;

import com.hc.medicdex.dto.PatientDto;
import com.hc.medicdex.entity.PatientEntity;

import java.util.Optional;

public interface PatientMapper extends EntityMapper<PatientDto, PatientEntity> {

    PatientDto toDto(Optional<PatientEntity> byId);
}