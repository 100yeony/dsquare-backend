package com.ktds.dsquare.board.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card,Long>, JpaSpecificationExecutor<Card>{
    List<Card>  findByDeleteYnOrderByCreateDateDesc(Boolean deleteYn);
}
