package com.hc.medicdex.dto;

import lombok.*;

import java.io.Serializable;

/**
 * A DTO for the {@link com.hc.medicdex.entity.StaffEntity} entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class StaffEntityDto implements Serializable {
    private Integer id;
    private String staffName;
    private String specialization;
    private Integer fee;
    private String staffType;
    private String phone;
    private String timing;
    private Integer hospital_id;
}