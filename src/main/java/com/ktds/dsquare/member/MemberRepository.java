package com.ktds.dsquare.member;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    List<Member> findAll(Specification<Member> spec);
    Optional<Member> findByEmailAndWithdrawDate(String email, LocalDateTime withdrawDate);
    Optional<Member> findByIdAndWithdrawDate(Long id, LocalDateTime withdrawDate);

    //궁금해요 검색 기능 관련
    List<Member> findByNameContaining(String name);

     //소통해요 사용자 검색 관련
    List<Member> findByNicknameContaining(String nickname);

     boolean existsByNickname(String nickname);

}
