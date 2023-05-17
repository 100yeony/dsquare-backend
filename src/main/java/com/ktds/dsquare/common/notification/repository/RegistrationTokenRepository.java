package com.ktds.dsquare.common.notification.repository;

import com.ktds.dsquare.common.notification.RegistrationToken;
import com.ktds.dsquare.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationTokenRepository extends JpaRepository<RegistrationToken, Long> {

    List<RegistrationToken> findByOwner(Member owner);

    boolean existsByOwnerAndValue(Member owner, String value);

}
