package com.ktds.dsquare.board.talk;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.TagService;
import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.board.talk.dto.TalkRegisterRequest;
import com.ktds.dsquare.board.talk.dto.TalkResponse;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TalkService {

    private final MemberRepository memberRepository;
    private final TalkRepository talkRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final TagService tagService;
    private final LikeRepository likeRepository;

    // 소통해요 작성
    @Transactional
    public void createTalk(TalkRegisterRequest request, Member writer) {
        Talk talk = Talk.toEntity(request, writer);
        talkRepository.save(talk);
        tagService.insertNewTags(request.getTags(), talk);
    }

    // 소통해요 전체조회 + 검색
    public List<BriefTalkResponse> getTalks(Member user, String key, String value, String order, Pageable pageable){
        Pageable page;
        if(order.equals("create")){
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate").descending());
        } else if (order.equals("like")) {
            page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("likeCnt").descending());
        } else {
            throw new RuntimeException("Invalid order. Using create || like");
        }

        //deleteYn = false인 것만 조회
        Specification<Talk> filter = Specification.where(TalkSpecification.equalNotDeleted(false));

        //사용자 이름 검색(2글자로도 포함된 사람 검색 & 다른 조건과 모두 AND)
        if (key != null && key.equals("member") && value != null) {
            List<Member> members = memberRepository.findByNameContaining(value);
            try {
                if (members.size() > 0) {
                    List<Member> writerIds = new ArrayList<>();
                    for (Member M : members) {
                        Member m = memberRepository.findById(M.getId())
                                .orElseThrow(() -> new EntityNotFoundException("Member Not Found"));
                        writerIds.add(m);
                    }
                    filter = filter.and(TalkSpecification.inWriter(writerIds));
                } else {
                    // 매칭되는 멤버가 없으면 빈 리스트 반환
                    return Collections.emptyList();
                }
            } catch (RuntimeException ex) {
                // Exception이 발생한 경우 빈 리스트 반환
                return Collections.emptyList();
            }
        }
        //제목+내용 검색
        if(key!=null && key.equals("titleAndContent") && value != null){
            filter = filter.and(TalkSpecification.equalTitleAndContentContaining(value));
        }

        Page<Talk> talkList = talkRepository.findAll(filter, page);
        List<BriefTalkResponse> searchResults = new ArrayList<>();

        for(Talk t : talkList){
            Boolean likeYn = findLikeYn(BoardType.TALK, t.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.TALK, t.getId());
            searchResults.add(BriefTalkResponse.toDto(t, commentCnt, t.getLikeCnt(), likeYn));
        }

        return searchResults;
    }

    // 소통해요 상세조회
    public TalkResponse getTalkDetail(Long talkId, Member user) {
        Talk talk = talkRepository.findByDeleteYnAndId(false, talkId);
        if(talk == null) throw new EntityNotFoundException("Talk Not Found. Talk ID :"+talkId);
        talk.increaseViewCnt();
        talkRepository.save(talk);

        Boolean likeYn = findLikeYn(BoardType.TALK, talkId, user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.TALK, talkId);

        return TalkResponse.toDto(talk, commentCnt, talk.getLikeCnt(), likeYn);
    }

    // 소통해요 수정
    @Transactional
    public void updateTalk(Long talkId, TalkRegisterRequest request) {
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
                tagService.deleteTagRelation(talk, oldTag);
        }
        tagService.insertNewTags(newTags, talk);
    }

    // 소통해요 삭제
    @Transactional
    public void deleteTalk(Long talkId) {
        Talk talk = talkRepository.findByDeleteYnAndId(false, talkId);
        //연관관계 삭제
        for(TalkTag oldTT : talk.getTalkTags()) {
            tagService.deleteTagRelation(talk, oldTT.getTag());
        }
        talk.deleteTalk();
        commentService.deleteCommentCascade(BoardType.TALK, talkId);
    }

    public void like(Talk talk) {
        talk.like();
        talkRepository.save(talk);
    }

    public void cancleLike(Talk talk){
        talk.cancleLike();
        talkRepository.save(talk);
    }

    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }


}
