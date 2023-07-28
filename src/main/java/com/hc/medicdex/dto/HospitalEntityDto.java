package com.hc.medicdex.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link com.hc.medicdex.entity.HospitalEntity} entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalEntityDto implements Serializable {
    private Integer id;
    private String hospitalName;
    private String ownerName;
    private String hospitalAddress;
    private String landLine_PTCL;
    private String mobilePhone;
    private Integer user_id;
}