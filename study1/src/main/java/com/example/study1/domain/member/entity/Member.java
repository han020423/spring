package com.example.study1.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;


    @Builder
    private Member(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;

    }

    public void updatePassword(String password) {
        if(password.equals(this.password)) {
            throw new IllegalArgumentException("기존 비밀번호와 동일합니다");
        }

        this.password = password;
    }

}
