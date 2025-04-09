package com.example.study1.domain.member.repository;

import com.example.study1.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByPassword(String password);

    boolean existsByEmail(String email);

}
