package com.ktds.dsquare.board.carrot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrotRepository extends JpaRepository<Carrot,Long>, JpaSpecificationExecutor<Carrot> {
    Carrot findByDeleteYnAndId(Boolean deleteYn, Long carrotId);
}
