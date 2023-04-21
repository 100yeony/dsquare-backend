package com.ktds.dsquare.board.card;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.card.dto.CardRequest;
import com.ktds.dsquare.board.card.dto.CardResponse;
import com.ktds.dsquare.board.card.dto.CardSelectionInfo;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final TeamRepository teamRepository;

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

    //read - 카드주세요 글 전체 조회
    public List<BriefCardResponse> getAllCards(){
        List<Card> cards = cardRepository.findByDeleteYnOrderByCreateDateDesc(false);
        List<BriefCardResponse> briefCards = new ArrayList<>();

        for(Card C : cards){
            Member member = C.getCardWriter();
            Team team = C.getProjTeam();

            Member owner = C.getCardOwner();
            CardSelectionInfo selectionInfo;
            if(owner != null){
                MemberInfo cardOwner = MemberInfo.toDto(owner);
                selectionInfo = CardSelectionInfo.toDto(C, cardOwner);
            }else{
                selectionInfo = null;
            }
            briefCards.add(BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(team), selectionInfo));
        }
        return briefCards;
    }

    //read - 카드주세요 글 상세 조회
    public CardResponse getCardDetail(Long cardId) {
        Card card = cardRepository.findByDeleteYnAndCardId(false, cardId);
        card.increaseViewCnt();
        cardRepository.save(card);
        return CardResponse.toDto(card, card.getCardWriter(), card.getProjTeam(), card.getCardOwner());
    }

    //update - 카드주세요 선정
    @Transactional
    public void giveCard(Long cardId, Long cardOwnerId){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));
        Member owner = memberRepository.findById(cardOwnerId)
                .orElseThrow(() -> new EntityNotFoundException("member is not found"));

        LocalDateTime now = LocalDateTime.now();
        card.selectCard(owner, true, now);
    }

    //update - 카드주세요 글 수정
    @Transactional
    public void updateCard(Long cardId, CardRequest request){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));

        Team projTeam = teamRepository.findById(request.getProjTeamId())
                .orElseThrow(() -> new EntityNotFoundException("team is not found"));

        LocalDateTime now = LocalDateTime.now();
        card.updateCard(projTeam, request.getTitle(), request.getContent(), request.getTeammate(), now);
    }

    //delete - 카드주세요 글 삭제
    @Transactional
    public void deleteCard(Long cardId){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));
        card.deleteCard();
    }

    //search - 카드주세요 검색
    public List<BriefCardResponse> searchCard(Long projTeamId){
        Team team = teamRepository.findById(projTeamId)
                .orElseThrow(()-> new EntityNotFoundException("team not found"));

        List<Card> cards = cardRepository.findByDeleteYnAndProjTeamOrderByCreateDateDesc(false, team);
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
            briefCards.add(BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(team), selectionInfo));
        }
        return briefCards;
    }
}
