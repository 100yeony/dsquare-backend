package com.ktds.dsquare.board.talk;

import com.ktds.dsquare.member.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TalkRepository extends JpaRepository<Talk,Long>{
    Talk findByDeleteYnAndId(boolean deleteYn, Long talkId);
    List<Talk> findByDeleteYnOrderByCreateDateDesc(boolean deleteYn);
    Optional<Talk> findById(Long id);
    List<Talk> findAll(Specification<Talk> filter, Sort sort);

    List<Talk> findByDeleteYnAndWriter(Boolean deleteYn, Member writer);

}
