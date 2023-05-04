package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.tag.CarrotTag;
import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.WeeklyTagDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarrotTagRepository extends JpaRepository<CarrotTag,Long> {
    void deleteByCarrotAndTag(Carrot carrot, Tag tag);
    @Query("SELECT NEW com.ktds.dsquare.board.tag.WeeklyTagDTO(ct.tag.id, COUNT(ct.tag.id), MAX(ct.createDate)) FROM CarrotTag ct WHERE ct.createDate >= :weekAgo GROUP BY ct.tag")
    List<WeeklyTagDTO> findTagsWithinLastWeek(@Param("weekAgo") LocalDateTime weekAgo);

}
