package com.ktds.dsquare.board.card;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.card.dto.CardRequest;
import com.ktds.dsquare.board.card.dto.CardResponse;
import com.ktds.dsquare.board.card.dto.CardSelectionInfo;
import com.ktds.dsquare.board.comment.CommentService;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.like.LikeService;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import com.ktds.dsquare.member.dto.response.TeamInfo;
import com.ktds.dsquare.member.team.Team;
import com.ktds.dsquare.member.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final TeamRepository teamRepository;
    private final LikeService likeService;
    private final CommentService commentService;

    //create - 카드주세요 글 작성
    @Transactional
    public void createCard(CardRequest dto) {
        Member cardWriter = memberRepository.findById(dto.getCardWriterId())
                .orElseThrow(() -> new EntityNotFoundException("cardWriter is not found"));

        Team projTeam = teamRepository.findById(dto.getProjTeamId())
                .orElseThrow(() -> new EntityNotFoundException("team is not found"));

        Card card = Card.toEntity(dto, cardWriter, projTeam);
        cardRepository.save(card);
    }

    //read - 카드주세요 글 전체 조회 & 검색
    public List<BriefCardResponse> getCards(Long projTeamId, Member user){
        List<BriefCardResponse> briefCards = new ArrayList<>();
        List<Card> cards;
        Team team;

        if(projTeamId != null){
            //검색
            team = teamRepository.findById(projTeamId)
                    .orElseThrow(()-> new EntityNotFoundException("team not found"));
            cards = cardRepository.findByDeleteYnAndProjTeamOrderByCreateDateDesc(false, team);
        }else{
            //전체조회
            cards = cardRepository.findByDeleteYnOrderByCreateDateDesc(false);
        }

        for(Card C : cards){
            Member member = C.getCardWriter();
            Member owner = C.getCardOwner();
            CardSelectionInfo selectionInfo;
            if(projTeamId == null){
                //전체조회
                team = C.getProjTeam();
            }else{
                team = teamRepository.findById(projTeamId)
                        .orElseThrow(()-> new EntityNotFoundException("team not found"));
            }
            if(owner != null){
                MemberInfo cardOwner = MemberInfo.toDto(owner);
                selectionInfo = CardSelectionInfo.toDto(C, cardOwner);
            }else{
                selectionInfo = null;
            }

            Long likeCnt = likeService.findLikeCnt(BoardType.CARD, C.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.CARD, C.getId(), user);
            Long commentCnt = (long) commentService.getAllComments("card", C.getId()).size();
            briefCards.add(BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(team), selectionInfo, likeCnt, likeYn, commentCnt));
        }

        return briefCards;
    }

    //read - 카드주세요 글 상세 조회
    public CardResponse getCardDetail(Long cardId, Member user) {
        Card card = cardRepository.findByDeleteYnAndId(false, cardId);
        card.increaseViewCnt();
        cardRepository.save(card);

        Long likeCnt = likeService.findLikeCnt(BoardType.CARD, card.getId());
        Boolean likeYn = likeService.findLikeYn(BoardType.CARD, card.getId(), user);
        Long commentCnt = (long) commentService.getAllComments("card", cardId).size();
        return CardResponse.toDto(card, card.getCardWriter(), card.getProjTeam(), card.getCardOwner(), likeCnt, likeYn, commentCnt);
    }

    //update - 카드주세요 선정
    @Transactional
    public void giveCard(Long cardId, Member user){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));
        card.selectCard(user, true);
    }

    //update - 카드주세요 글 수정
    @Transactional
    public void updateCard(Long cardId, CardRequest request){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));

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
    }


    //read - 이달의 카드 전체 조회
    public List<BriefCardResponse> selectedCardList(){

        List<Card> cards = cardRepository.findSelectedCard();
        List<BriefCardResponse> briefCards = new ArrayList<>();

        for(Card C : cards){
            Member member = C.getCardWriter();
            Member owner = C.getCardOwner();
            CardSelectionInfo selectionInfo;

            if(owner != null){
                MemberInfo cardOwner = MemberInfo.toDto(owner);
                selectionInfo = CardSelectionInfo.toDto(C, cardOwner);
            }else{
                selectionInfo = null;
            }
            Long likeCnt = likeService.findLikeCnt(BoardType.CARD, C.getId());
            Boolean likeYn = likeService.findLikeYn(BoardType.CARD, C.getId(), C.getCardWriter());
            Long commentCnt = (long) commentService.getAllComments("card", C.getId()).size();
            briefCards.add(BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(C.getProjTeam()), selectionInfo, likeCnt, likeYn, commentCnt));
        }
        return briefCards;
    }
}
