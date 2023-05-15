package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.tag.QuestionTag;
import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.WeeklyTagDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag,Long> {
    void deleteByPostAndTag(Question question, Tag tag);
    @Query("SELECT NEW com.ktds.dsquare.board.tag.WeeklyTagDTO(qt.tag.id, COUNT(qt.tag.id), MAX(qt.createDate)) FROM QuestionTag qt WHERE qt.createDate >= :weekAgo GROUP BY qt.tag")
    List<WeeklyTagDTO> findTagsWithinLastWeek(@Param("weekAgo") LocalDateTime weekAgo);

}
