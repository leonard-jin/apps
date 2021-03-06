package com.clone.apps.domains.members.persistences.entity;


import com.clone.apps.commons.entity.Auditable;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by kh.jin on 2019. 7. 15.
 */
@Entity
@Table(name = "tb_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(callSuper = false)
public class Member extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    @Getter
    private Long id;

    @Column(name = "email")
    @Email
    @Getter
    private String email;

    @Column(name = "member_nm")
    @NotEmpty
    @Getter
    private String name;

    @Column(name = "birthday")
    @Getter
    private String birthday;

    @Column(name = "telecom_company_cd")
    @Getter
    private String telecomCompanyCode;

    @Column(name = "mobile_number")
    @Getter
    private String mobileNumber;

    @Column(name = "profile_image_url")
    @Getter
    private String profileImageUrl;

    @OneToOne
    @JoinColumn(name = "member_id")
    @Getter
    private MemberAuthentication memberAuthentication;

    @Builder
    public Member(String email, String name, String birthday, String telecomCompanyCode, String mobileNumber, String profileImageUrl) {
        super();
        this.email = email;
        this.name = name;
        this. birthday = birthday;
        this.telecomCompanyCode = telecomCompanyCode;
        this.mobileNumber = mobileNumber;
        this. profileImageUrl = profileImageUrl;
    }
}