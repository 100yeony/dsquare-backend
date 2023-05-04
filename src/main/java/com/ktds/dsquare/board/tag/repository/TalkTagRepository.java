package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.tag.WeeklyTagDTO;
import com.ktds.dsquare.board.talk.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TalkTagRepository extends JpaRepository<TalkTag,Long> {
    void deleteByTalkAndTag(Talk talk, Tag tag);
    @Query("SELECT NEW com.ktds.dsquare.board.tag.WeeklyTagDTO(tt.tag.id, COUNT(tt.tag.id), MAX(tt.createDate)) FROM TalkTag tt WHERE tt.createDate >= :weekAgo GROUP BY tt.tag")
    List<WeeklyTagDTO> findTagsWithinLastWeek(@Param("weekAgo") LocalDateTime weekAgo);

}
