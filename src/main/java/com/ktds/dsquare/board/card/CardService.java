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
        Card card = new Card();
        Member cardWriter = memberRepository.findById(dto.getCardWriterId())
                .orElseThrow(() -> new RuntimeException("cardWriter is not found"));
        card.setCardWriter(cardWriter);
        Team projTeam = teamRepository.findById(dto.getProjTeamId())
                .orElseThrow(() -> new EntityNotFoundException("team is not found"));
        card.setProjTeam(projTeam);

        card.setTitle(dto.getTitle());
        card.setContent(dto.getContent());
        card.setTeammate(dto.getTeammate());

        LocalDateTime now = LocalDateTime.now();
        card.setCreateDate(now);
        card.setLastUpdateDate(now);

        card.setViewCnt(0L);
        card.setDeleteYn(false);
        cardRepository.save(card);
    }

    //read - 카드주세요 글 전체 조회
    public List<BriefCardResponse> getAllCards(){
        List<Card> cards = cardRepository.findByDeleteYnOrderByCreateDateDesc(false);
        List<BriefCardResponse> briefCards = new ArrayList<>();

        for(Card C : cards){
            Member member = C.getCardWriter();
            Team team = C.getProjTeam();

            briefCards.add(BriefCardResponse.toDto(C, MemberInfo.toDto(member), TeamInfo.toDto(team)));
        }
        return briefCards;
    }

    //read - 카드주세요 글 상세 조회
    public CardResponse getCardDetail(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));
        card.increaseViewCnt();
        cardRepository.save(card);

        Member member = card.getCardWriter();
        MemberInfo writer = MemberInfo.toDto(member);

        Team team = card.getProjTeam();
        TeamInfo teamInfo = TeamInfo.toDto(team);

        Member owner = card.getCardOwner();
        CardSelectionInfo selectionInfo;

        if(owner != null){
            MemberInfo cardOwner = MemberInfo.toDto(owner);
            selectionInfo = CardSelectionInfo.toDto(card, cardOwner);
        }else{
            selectionInfo = null;
        }

        return CardResponse.toDto(card, writer, teamInfo, selectionInfo);
    }

    //update - 카드주세요 선정
    @Transactional
    public void giveCard(Long cardId, Long cardOwnerId){
        Card card = cardRepository.findById(cardId)
                .orElseThrow(()-> new EntityNotFoundException("card is not found"));
        Member owner = memberRepository.findById(cardOwnerId)
                .orElseThrow(() -> new EntityNotFoundException("member is not found"));
        card.setCardOwner(owner);
        card.setSelectionYn(true);
        LocalDateTime now = LocalDateTime.now();
        card.setSelectedDate(now);
    }

}
