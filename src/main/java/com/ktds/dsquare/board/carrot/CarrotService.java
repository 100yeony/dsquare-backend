package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.carrot.dto.CarrotRegisterRequest;
import com.ktds.dsquare.board.carrot.dto.CarrotResponse;
import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeService;
import com.ktds.dsquare.board.tag.CarrotTag;
import com.ktds.dsquare.board.tag.Tag;
import com.ktds.dsquare.board.tag.repository.CarrotTagRepository;
import com.ktds.dsquare.board.tag.repository.TagRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
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
public class CarrotService {

    private final CarrotRepository carrotRepository;
    private final LikeService likeService;
    private final CommentService commentService;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final CarrotTagRepository carrotTagRepository;
    private final CommentRepository commentRepository;

    //create - 당근해요 글 작성
    @Transactional
    public void createCarrot(CarrotRegisterRequest dto, Member user){
        Carrot carrot = Carrot.toEntity(dto, user);
        carrotRepository.save(carrot);
        insertNewTags(dto.getTags(), carrot);
    }

    //read - 당근해요 글 전체 조회 & 검색
    public List<BriefCarrotResponse> getCarrots(Member user, String key, String value){
        //deleteYn = false인 것만 조회
        Specification<Carrot> filter = Specification.where(CarrotSpecification.equalNotDeleted(false));
        //업무 구분


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
                    filter = filter.and(CarrotSpecification.inWriter(writerIds));
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
            filter = filter.and(CarrotSpecification.equalTitleAndContentContaining(value));
        }

        List<Carrot> carrotList = carrotRepository.findAll(filter, Sort.by(Sort.Direction.DESC, "createDate"));
        List<BriefCarrotResponse> searchResults = new ArrayList<>();

        //BriefCarrotResponse 객체로 만들어줌
        for(Carrot C: carrotList){
            Long likeCnt = likeService.findLikeCnt(BoardType.CARROT, C.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.CARROT, C.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARROT, C.getId());
            searchResults.add(BriefCarrotResponse.toDto(C, MemberInfo.toDto(C.getWriter()), likeCnt, likeYn, commentCnt));
        }

        return searchResults;
    }

    //read - 당근해요 글 상세 조회
    public CarrotResponse getCarrotDetail(Long carrotId, Member user){
        Carrot carrot = carrotRepository.findByDeleteYnAndId(false, carrotId);
        carrot.increaseViewCnt();
        carrotRepository.save(carrot);

        Long likeCnt = likeService.findLikeCnt(BoardType.CARROT, carrot.getId());
        Boolean likeYn = likeService.findLikeYn(BoardType.CARROT, carrot.getId(), user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARROT, carrot.getId());
        return CarrotResponse.toDto(carrot, carrot.getWriter(), likeCnt, likeYn, commentCnt);
    }

    //update - 당근해요 글 수정
    @Transactional
    public void updateCarrot(Long carrotId, CarrotRegisterRequest request){
        Carrot carrot = carrotRepository.findByDeleteYnAndId(false, carrotId);
        carrot.updateCarrot(request);

        // 태그 수정
        List<CarrotTag> oldCTs = carrot.getCarrotTags();
        List<Tag> oldTags = new ArrayList<>();
        for(CarrotTag oldCT : oldCTs) {
            oldTags.add(oldCT.getTag());
        }
        List<String> newTags = request.getTags();

        for(Tag oldTag : oldTags) {
            String oldTagName = oldTag.getName();
            if(newTags.contains(oldTagName))
                newTags.remove(oldTagName);
            else
                deleteCarrotTagRelation(carrot, oldTag);
        }
        insertNewTags(newTags, carrot);

    }

    //delete - 당근해요 글 삭제
    @Transactional
    public void deleteCarrot(Long carrotId){
        Carrot carrot = carrotRepository.findById(carrotId)
                .orElseThrow(()-> new EntityNotFoundException("carrot is not found"));
        carrot.deleteCarrot();
        commentService.deleteCommentCascade(BoardType.CARROT, carrotId);
    }


    // 새 태그(키워드) 등록
    @Transactional
    public void insertNewTags(List<String> newTags, Carrot carrot) {
        for (String name : newTags) {
            Tag tag = tagRepository.findByName(name);
            if(tag == null) {
                tag = Tag.toEntity(name);
                tagRepository.save(tag);
            }
            CarrotTag ct = CarrotTag.toEntity(carrot, tag);
            carrotTagRepository.save(ct);
        }
    }

    // 태그-질문 간 연관관계 삭제
    @Transactional
    public void deleteCarrotTagRelation(Carrot carrot, Tag tag) {
        carrotTagRepository.deleteByCarrotAndTag(carrot, tag);
    }

}
