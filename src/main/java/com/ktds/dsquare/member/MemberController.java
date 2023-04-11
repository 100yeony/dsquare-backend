package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
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


    @PostMapping("/account/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        log.debug("MemberController ----> signup");
        return new ResponseEntity<>(memberService.insertMember(signupRequest), HttpStatus.OK);
    }

    @GetMapping("/member/members")
    public ResponseEntity<List<BriefMemberInfo>> getAllMembers(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(memberService.selectAllMembers(params), HttpStatus.OK);
    }

}
