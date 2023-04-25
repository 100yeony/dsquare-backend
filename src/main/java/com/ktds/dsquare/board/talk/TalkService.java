package com.ktds.dsquare.board.talk;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeService;
import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.tag.repository.TagRepository;
import com.ktds.dsquare.board.tag.repository.TalkTagRepository;
import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.board.talk.dto.TalkRequest;
import com.ktds.dsquare.board.talk.dto.TalkResponse;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TalkService {

    private final MemberRepository memberRepository;
    private final TalkRepository talkRepository;
    private final CommentRepository commentRepository;
    private final LikeService likeService;
    private final TagRepository tagRepository;
    private final TalkTagRepository talkTagRepository;

    // 소통해요 작성
    @Transactional
    public void createTalk(TalkRequest request) {
        Member writer = memberRepository.findById(request.getWriterId()).orElseThrow(() -> new RuntimeException("Writer Not Found"));
        Talk talk = Talk.toEntity(request, writer);
        talkRepository.save(talk);
        insertNewTalkTags(request.getTags(), talk);
    }

    // 소통해요 전체조회
    public List<BriefTalkResponse> getAllTalks(Member user) {
        List<Talk> talks = talkRepository.findByDeleteYnOrderByCreateDateDesc(false);
        List<BriefTalkResponse> briefTalks = new ArrayList<>();
        for(Talk t : talks) {
            Integer likeCnt = likeService.findLikeCnt(BoardType.TALK, t.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.TALK, t.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.TALK, t.getId());

            briefTalks.add(BriefTalkResponse.toDto(t, commentCnt, likeCnt, likeYn));
        }
        return briefTalks;
    }

    // 소통해요 상세조회
    public TalkResponse getTalkDetail(Long talkId, Member user) {
        Talk talk = talkRepository.findById(talkId).orElseThrow(()->new RuntimeException("Talk Not Found"));
        talk.increaseViewCnt();
        talkRepository.save(talk);

        Integer likeCnt = likeService.findLikeCnt(BoardType.TALK, talkId);
        Boolean likeYn = likeService.findLikeYn(BoardType.TALK, talkId, user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.TALK, talkId);

        return TalkResponse.toDto(talk, commentCnt, likeCnt, likeYn);
    }

    // 소통해요 수정
    @Transactional
    public void updateTalk(Long talkId, TalkRequest request) {
        Talk talk = talkRepository.findByDeleteYnAndId(false, talkId);
        if(talk == null) throw new EntityNotFoundException("Talk Not Found. Talk ID :"+talkId);
        talk.updateTalk(request.getTitle(), request.getContent());

        // 태그 수정
        List<TalkTag> oldTTs = talk.getTalkTags();
        List<Tag> oldTags = new ArrayList<>();
        for(TalkTag oldTT : oldTTs) {
            oldTags.add(oldTT.getTag());
        }
        List<String> newTags = request.getTags();

        for(Tag oldTag : oldTags) {
            String oldTagName = oldTag.getName();
            if(newTags.contains(oldTagName))
                newTags.remove(oldTagName);
            else
                deleteTalkTagRelation(talk, oldTag);
        }
        insertNewTalkTags(newTags, talk);
    }

    // 소통해요 삭제
    @Transactional
    public void deleteTalk(Long talkId) {
        Talk talk = talkRepository.findByDeleteYnAndId(false, talkId);
        //연관관계 삭제
        for(TalkTag oldTT : talk.getTalkTags()) {
            deleteTalkTagRelation(talk, oldTT.getTag());
        }
        talk.deleteTalk();
    }

    // 새 태그(키워드) 등록
    @Transactional
    public void insertNewTalkTags(List<String> newTags, Talk talk) {
        for (String name : newTags) {
            Tag tag = tagRepository.findByName(name);
            if(tag == null) {
                tag = Tag.toEntity(name);
                tagRepository.save(tag);
            }
            TalkTag tt = TalkTag.toEntity(talk, tag);
            talkTagRepository.save(tt);
        }
    }

    // 태그-질문 간 연관관계 삭제
    @Transactional
    public void deleteTalkTagRelation(Talk talk, Tag tag) {
        talkTagRepository.deleteByTalkAndTag(talk, tag);
    }

    // search? 있움?

}
