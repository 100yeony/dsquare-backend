package com.ktds.dsquare.board.card;

import com.ktds.dsquare.board.card.dto.BriefCardResponse;
import com.ktds.dsquare.board.card.dto.CardRequest;
import com.ktds.dsquare.board.card.dto.CardResponse;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> createCard(@RequestBody CardRequest request){
        cardService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //read - 카드주세요 글 전체 목록 조회
    @GetMapping("board/cards")
    public ResponseEntity<List<BriefCardResponse>> getAllCards(){
        return new ResponseEntity<>(cardService.getAllCards(), HttpStatus.OK);
    }

    //read - 카드주세요 글 상세 조회
    @GetMapping("board/cards/{cardId}")
    public ResponseEntity<CardResponse> getCardDetail(@PathVariable("cardId") Long cardId){
        return new ResponseEntity<>(cardService.getCardDetail(cardId), HttpStatus.OK);
    }

    //update - 카드주세요 선정
    @PostMapping("board/cards/{cardId}/cardOwner/{cardOwnerId}")
    public ResponseEntity<Void> giveCard(@PathVariable("cardId") Long cardId, @PathVariable("cardOwnerId") Long cardOwnerId){
        cardService.giveCard(cardId, cardOwnerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //update - 카드주세요 글 수정
    @PostMapping("board/cards/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable("cardId") Long cardId, @RequestBody CardRequest request){
        cardService.updateCard(cardId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //delete - 카드주세요 글 삭제
    @DeleteMapping("board/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable("cardId") Long cardId){
        cardService.deleteCard(cardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //search - 카드주세요 검색
    @GetMapping("board/cards/search")
    public ResponseEntity<List<BriefCardResponse>> searchCard(@RequestParam("projTeamId") Long projTeamId){
        return new ResponseEntity<>(cardService.searchCard(projTeamId), HttpStatus.OK);
    }
}
