package com.hc.medicdex.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Entity
@Table(name = "h_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "varchar(255) default 'null'")
    private String hospitalId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String authority;
    private String phone;
    private String address;
    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private HospitalEntity hospital;
}
