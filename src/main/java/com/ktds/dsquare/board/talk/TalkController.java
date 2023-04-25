package com.ktds.dsquare.board.talk;

import com.ktds.dsquare.board.talk.dto.BriefTalkResponse;
import com.ktds.dsquare.board.talk.dto.TalkRequest;
import com.ktds.dsquare.board.talk.dto.TalkResponse;
import com.ktds.dsquare.common.annotatin.AuthUser;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TalkController {

    private final TalkService talkService;

    // 소통해요 작성
    @PostMapping("/board/talk")
    public ResponseEntity<Void> createTalk(@RequestBody TalkRequest request){
        talkService.createTalk(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 소통해요 전체조회(정렬포함)
    @GetMapping("/board/talk")
    public ResponseEntity<List<BriefTalkResponse>> getAllTalks(@AuthUser Member user){
        return new ResponseEntity<>(talkService.getAllTalks(user), HttpStatus.OK);
    }

    // 소통해요 상세조회
    @GetMapping("/board/talk/{talkId}")
    public ResponseEntity<TalkResponse> getTalkDetail(@PathVariable Long talkId, @AuthUser Member user){
        return ResponseEntity.ok(talkService.getTalkDetail(talkId, user));
    }

    // 소통해요 수정
    @PostMapping("/board/talk/{talkId}")
    public ResponseEntity<Void> updateTalk(@PathVariable Long talkId, @RequestBody TalkRequest request){
        talkService.updateTalk(talkId, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 소통해요 삭제
    @DeleteMapping("/board/talk/{talkId}")
    public ResponseEntity<Void> deleteTalk(@PathVariable Long talkId) {
        talkService.deleteTalk(talkId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 소통해요 검색(제목+내용, 사용자(이름))
    @GetMapping("/board/talk/search")
    public ResponseEntity<List<BriefTalkResponse>> searchTalk(@AuthUser Member user, @RequestParam(required = false) String key, @RequestParam(required = false) String value) {
        return new ResponseEntity<>(talkService.searchTalk(user, key, value), HttpStatus.OK);
    }
}
