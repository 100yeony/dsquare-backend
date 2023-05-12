package com.ktds.dsquare.board.talk;

import com.ktds.dsquare.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalkRepository extends JpaRepository<Talk,Long>{
    Optional<Talk> findByDeleteYnAndId(boolean deleteYn, Long talkId);

    //전체조회 & 검색 관련
    Page<Talk> findAll(Specification<Talk> filter, Pageable pageable);

    //마이페이지 관련
    Page<Talk> findByDeleteYnAndWriter(Boolean deleteYn, Member writer, Pageable pageable);

}
