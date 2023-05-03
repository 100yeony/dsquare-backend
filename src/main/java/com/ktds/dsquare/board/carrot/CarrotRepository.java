package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarrotRepository extends JpaRepository<Carrot,Long>, JpaSpecificationExecutor<Carrot> {
    Carrot findByDeleteYnAndId(Boolean deleteYn, Long carrotId);

    //마이페이지 관련
    List<Carrot> findByDeleteYnAndWriter(Boolean deleteYn, Member writer);
}
