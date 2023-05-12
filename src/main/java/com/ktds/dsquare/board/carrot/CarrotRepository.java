package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrotRepository extends JpaRepository<Carrot,Long>, JpaSpecificationExecutor<Carrot> {
    Optional<Carrot> findByDeleteYnAndId(Boolean deleteYn, Long carrotId);

    //마이페이지 관련
    Page<Carrot> findByDeleteYnAndWriter(Boolean deleteYn, Member writer, Pageable pageable);

    Page<Carrot> findAll(Specification<Carrot> filter, Pageable pageable);
}
