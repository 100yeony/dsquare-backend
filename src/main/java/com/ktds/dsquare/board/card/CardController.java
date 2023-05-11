package com.ktds.dsquare.board.card;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.card.dto.CardRegisterRequest;
import com.ktds.dsquare.board.card.dto.CardResponse;
import com.ktds.dsquare.board.card.dto.CardUpdateRequest;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    //create - 카드주세요 글 작성
    @PostMapping("/board/cards")
    public ResponseEntity<Void> createCard(@RequestBody CardRegisterRequest request, @AuthUser Member user){
        cardService.createCard(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //read - 카드주세요 글 전체 목록 조회 & 검색
    @GetMapping("board/cards")
    public ResponseEntity<List<BriefCardResponse>> getCards(@RequestParam(required = false) Boolean selection, @RequestParam(required = false) Long projTeamId,
                                                            @AuthUser Member user, @RequestParam(required = false) String order, Pageable pageable){
        return new ResponseEntity<>(cardService.getCards(selection, projTeamId, user, order, pageable), HttpStatus.OK);
    }

    //read - 카드주세요 글 상세 조회
    @GetMapping("board/cards/{cardId}")
    public ResponseEntity<CardResponse> getCardDetail(@PathVariable("cardId") Long cardId, @AuthUser Member user){
        return new ResponseEntity<>(cardService.getCardDetail(cardId, user), HttpStatus.OK);
    }

    //update - 카드주세요 글 수정
    @PatchMapping("board/cards/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable("cardId") Long cardId, @RequestBody(required = false) CardUpdateRequest request){
            cardService.updateCard(cardId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //update - 카드주세요 선정
    @PatchMapping("board/cards/{cardId}/chosen")
    public ResponseEntity<Void> giveCard(@PathVariable("cardId") Long cardId, @AuthUser Member user){
        cardService.giveCard(cardId, user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //delete - 카드주세요 글 삭제
    @DeleteMapping("board/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable("cardId") Long cardId){
        cardService.deleteCard(cardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //read - 이달의 카드 조회
    @GetMapping("board/cards/card-of-the-month")
    public ResponseEntity<List<BriefCardResponse>> selectedCardList(@AuthUser Member user){
        return new ResponseEntity<>(cardService.selectedCardList(user), HttpStatus.OK);
    }
}
