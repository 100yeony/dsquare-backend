package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.TalkTag;
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
    @Query(value = "SELECT tag_id as id, COUNT(tag_id) AS tag_count, MAX(create_date) as create_date\n" +
                    "FROM talk_tag\n" +
                    "WHERE create_date >= :week_ago\n" +
                    "GROUP BY tag_id\n" +
                    "ORDER BY tag_count DESC", nativeQuery = true)
    List<Object[]> findTagsWithinLastWeek(@Param("week_ago") LocalDateTime weekAgo);

}
