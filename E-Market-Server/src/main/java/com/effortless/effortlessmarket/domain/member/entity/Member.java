package com.effortless.effortlessmarket.domain.member.entity;

import com.effortless.effortlessmarket.domain.memberAddress.entity.MemberAddress;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(of = {"id","name","gender","phoneNumber"})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_id")
    private Long id;

    @Column(name = "m_email")
    private String email;

    @Column(name = "m_password")
    private String password;

    @Column(name = "m_name")
    private String name;

    @Column(name = "m_gender")
    private String gender;

    @Column(name = "m_phone_number")
    private String phoneNumber;

    @Column(name = "m_address")
    private String address;

    @OneToMany(mappedBy = "member")
    private List<MemberAddress> memberAddresses = new ArrayList<>();

    /* 회원 생성 */
    public Member(String email, String password, String name, String gender, String phoneNumber, String address) {
        this.email = email;
        this.password = new BCryptPasswordEncoder(10).encode(password);
        this.name = name;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }


}
