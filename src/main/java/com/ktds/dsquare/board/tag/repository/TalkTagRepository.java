package com.ktds.dsquare.board.tag.repository;

import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.talk.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TalkTagRepository extends JpaRepository<TalkTag,Long> {
    void deleteByTalkAndTag(Talk talk, Tag tag);

}
