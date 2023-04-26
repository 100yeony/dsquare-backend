package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.carrot.dto.CarrotRequest;
import com.ktds.dsquare.board.carrot.dto.CarrotResponse;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeService;
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

    //create - 당근해요 글 작성
    @Transactional
    public void createCarrot(CarrotRequest dto, Member user){
        Carrot carrot = Carrot.toEntity(dto, user);
        carrotRepository.save(carrot);
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
            Boolean likeYn = likeService.findLikeYn(BoardType.CARROT, C.getId(), C.getCarrotWriter());
//            Long commentCnt = (long) commentService.getAllComments("carrot", C.getId()).size();
            searchResults.add(BriefCarrotResponse.toDto(C, MemberInfo.toDto(C.getCarrotWriter()), likeCnt, likeYn, null));
        }

        return searchResults;
    }

    //read - 당근해요 글 상세 조회
    public CarrotResponse getCarrotDetail(Long carrotId, Member user){
        Carrot carrot = carrotRepository.findByDeleteYnAndId(false, carrotId);
        carrot.increaseViewCnt();
        carrotRepository.save(carrot);

        Long likeCnt = likeService.findLikeCnt(BoardType.CARD, carrot.getId());
        Boolean likeYn = likeService.findLikeYn(BoardType.CARD, carrot.getId(), user);
        Long commentCnt = (long) commentService.getAllComments("card", carrotId).size();
        return CarrotResponse.toDto(carrot, carrot.getCarrotWriter(), likeCnt, likeYn, commentCnt);
    }

    //update - 당근해요 글 수정
    @Transactional
    public void updateCarrot(Long carrotId, CarrotRequest request){
        Carrot carrot = carrotRepository.findByDeleteYnAndId(false, carrotId);
        carrot.updateCarrot(request);
    }

    //delete - 당근해요 글 삭제
    @Transactional
    public void deleteCarrot(Long carrotId){
        Carrot carrot = carrotRepository.findById(carrotId)
                .orElseThrow(()-> new EntityNotFoundException("carrot is not found"));
        carrot.deleteCarrot();
    }

}
