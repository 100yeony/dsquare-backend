package com.ktds.dsquare.board.card;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.card.dto.CardRegisterRequest;
import com.ktds.dsquare.board.card.dto.CardResponse;
import com.ktds.dsquare.board.card.dto.CardSelectionInfo;
import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.common.Paging.PagingService;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import com.ktds.dsquare.member.dto.response.TeamInfo;
import com.ktds.dsquare.member.team.Team;
import com.ktds.dsquare.member.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final TeamRepository teamRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final LikeRepository likeRepository;
    private final PagingService pagingService;

    //create - 카드주세요 글 작성
    @Transactional
    public void createCard(CardRegisterRequest dto, Member user) {
        Team projTeam = teamRepository.findById(dto.getProjTeamId())
                .orElseThrow(() -> new EntityNotFoundException("team is not found"));

        Card card = Card.toEntity(dto, user, projTeam);
        cardRepository.save(card);
    }

    //read - 카드주세요 글 전체 조회 & 검색
    public List<BriefCardResponse> getCards(Long projTeamId, Member user, String order, Pageable pageable){
        Pageable page = pagingService.orderPage(order, pageable);

        List<BriefCardResponse> briefCards = new ArrayList<>();
        Page<Card> cards;
        if(projTeamId != null){
            //검색
            Team team = teamRepository.findById(projTeamId)
                    .orElseThrow(()-> new EntityNotFoundException("team not found"));
            cards = cardRepository.findByDeleteYnAndProjTeamOrderByCreateDateDesc(false, team, page);
        }else{
            //전체조회
            cards = cardRepository.findByDeleteYnOrderByCreateDateDesc(false, page);
        }
        for(Card C : cards){
            briefCards.add(makeBriefCardRes(C, user, projTeamId));
        }
        return briefCards;
    }

    public BriefCardResponse makeBriefCardRes(Card C, Member user, Long projTeamId){
        Member member = C.getWriter();
        Member owner = C.getCardOwner();
        CardSelectionInfo selectionInfo;
        Team team;
        if(projTeamId == null){
            //전체조회
            team = C.getProjTeam();
        }else{
            //검색
            team = teamRepository.findById(projTeamId)
                    .orElseThrow(()-> new EntityNotFoundException("team not found"));
        }
        if(owner != null){
            MemberInfo cardOwner = MemberInfo.toDto(owner);
            selectionInfo = CardSelectionInfo.toDto(C, cardOwner);
        }else{
            selectionInfo = null;
        }
        Boolean likeYn = findLikeYn(BoardType.CARD, C.getId(), user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARD, C.getId());
        return BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(team), selectionInfo, C.getLikeCnt(), likeYn, commentCnt);
    }

    //read - 카드주세요 글 상세 조회
    public CardResponse getCardDetail(Long cardId, Member user) {
        Card card = cardRepository.findByDeleteYnAndId(false, cardId);
        card.increaseViewCnt();
        cardRepository.save(card);

        Boolean likeYn = findLikeYn(BoardType.CARD, card.getId(), user);
        Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARD, cardId);
        return CardResponse.toDto(card, card.getWriter(), card.getProjTeam(), card.getCardOwner(), card.getLikeCnt(), likeYn, commentCnt);
    }

    //update - 카드주세요 선정
    @Transactional
    public void giveCard(Long cardId, Member user){
        Card card = cardRepository.findByDeleteYnAndId(false, cardId);
        card.selectCard(user, true);
    }

    //update - 카드주세요 글 수정
    @Transactional
    public void updateCard(Long cardId, CardRegisterRequest request){
        Card card = cardRepository.findByDeleteYnAndId(false, cardId);
        Team projTeam = teamRepository.findById(request.getProjTeamId())
                .orElseThrow(() -> new EntityNotFoundException("team is not found"));

        card.updateCard(projTeam, request);
    }

    //delete - 카드주세요 글 삭제
    @Transactional
    public void deleteCard(Long cardId){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));
        card.deleteCard();
        commentService.deleteCommentCascade(BoardType.CARD, cardId);
    }

    //read - 이달의 카드 전체 조회
    public List<BriefCardResponse> selectedCardList(Member user){
        List<Card> cards = cardRepository.findSelectedCard();
        List<BriefCardResponse> briefCards = new ArrayList<>();

        for(Card C : cards){
            Member member = C.getWriter();
            Member owner = C.getCardOwner();
            CardSelectionInfo selectionInfo;

            if(owner != null){
                MemberInfo cardOwner = MemberInfo.toDto(owner);
                selectionInfo = CardSelectionInfo.toDto(C, cardOwner);
            }else{
                selectionInfo = null;
            }
            Boolean likeYn = findLikeYn(BoardType.CARD, C.getId(), user);
            Long commentCnt = commentRepository.countByBoardTypeAndPostId(BoardType.CARD, C.getId());
            briefCards.add(BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(C.getProjTeam()), selectionInfo, C.getLikeCnt(), likeYn, commentCnt));
        }
        return briefCards;
    }

    public void like(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("answer not found"));
        card.like();
    }


    public void cancleLike(Long id){
        Card card = cardRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("answer not found"));
        card.cancleLike();
    }

    public Boolean findLikeYn(BoardType boardType, Long postId, Member user){
        return likeRepository.existsByBoardTypeAndPostIdAndMember(boardType, postId, user);
    }
}
