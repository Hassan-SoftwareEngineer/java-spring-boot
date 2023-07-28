package com.hc.medicdex.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

/**
 * A DTO for the {@link com.hc.medicdex.entity.UserEntity} entity
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntityDto implements UserDetails {

    private Integer id;
    private String hospitalId;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String authority;
    private String phone;
    private String address;

    @Override
    public String getUsername() {
        return hospitalId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}