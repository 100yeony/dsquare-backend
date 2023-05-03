package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.board.carrot.dto.BriefCarrotResponse;
import com.ktds.dsquare.board.carrot.dto.CarrotRegisterRequest;
import com.ktds.dsquare.board.carrot.dto.CarrotResponse;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarrotController {

    private final CarrotService carrotService;

    //create - 당근해요 글 작성
    @PostMapping("/board/carrots")
    public ResponseEntity<Void> createCarrot(@RequestBody CarrotRegisterRequest request, @AuthUser Member user){
        carrotService.createCarrot(request, user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //read - 당근해요 글 전체 조회 & 검색
    @GetMapping("board/carrots")
    public List<BriefCarrotResponse> getCarrots(@AuthUser Member user,
                                                                @RequestParam(required = false) String key,
                                                                @RequestParam(required = false) String value){
        return carrotService.getCarrots(user, key, value);
    }

    //read - 당근해요 글 상세 조회
    @GetMapping("/board/carrots/{carrotId}")
    public ResponseEntity<CarrotResponse> getCarrotDetail(@PathVariable("carrotId") Long carrotId, @AuthUser Member user){
        return new ResponseEntity<>(carrotService.getCarrotDetail(carrotId, user), HttpStatus.OK);
    }

    //update - 당근해요 글 수정
    @PostMapping("/board/carrots/{carrotId}")
    public ResponseEntity<Void> updateCarrot(@PathVariable("carrotId") Long carrotId, @RequestBody CarrotRegisterRequest request){
        carrotService.updateCarrot(carrotId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //delete - 당근해요 글 삭제
    @DeleteMapping("/board/carrots/{carrotId}")
    public ResponseEntity<Void> deleteCarrot(@PathVariable("carrotId") Long carrotId){
        carrotService.deleteCarrot(carrotId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
