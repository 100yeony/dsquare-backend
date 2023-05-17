package com.ktds.dsquare.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawnMemberRepository extends JpaRepository<WithdrawnMember, Long> {

}
