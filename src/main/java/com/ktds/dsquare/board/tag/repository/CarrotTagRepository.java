package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.tag.CarrotTag;
import com.ktds.dsquare.board.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrotTagRepository extends JpaRepository<CarrotTag,Long> {
    void deleteByCarrotAndTag(Carrot carrot, Tag tag);

}
