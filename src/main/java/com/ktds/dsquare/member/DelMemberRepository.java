package com.ktds.dsquare.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelMemberRepository extends JpaRepository<DelMember, Long> {

}
