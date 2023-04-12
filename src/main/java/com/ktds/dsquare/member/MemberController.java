package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.MemberUpdateRequest;
import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;


    /**
     * 회원 가입
     */
    @PostMapping("/account/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        log.debug("MemberController ----> signup");
        return new ResponseEntity<>(memberService.insertMember(signupRequest), HttpStatus.OK);
    }

    /**
     * 전체 회원 조회 (간략한 정보)
     */
    @GetMapping("/member/members")
    public ResponseEntity<List<BriefMemberInfo>> getAllMembers(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(memberService.selectAllMembers(params), HttpStatus.OK);
    }
    /**
     * 회원 정보 조회 (상세 정보 조회)
     */
    @GetMapping("/member/members/{id}")
    public ResponseEntity<MemberInfo> getMember(@PathVariable Long id) {
        return new ResponseEntity<>(memberService.selectMember(id), HttpStatus.OK);
    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping("/member/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequest request) {
        return new ResponseEntity<>(memberService.updateMember(id, request), HttpStatus.OK);
    }

}
