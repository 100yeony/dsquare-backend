package com.ktds.dsquare.member;

import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.common.exception.MemberException;
import com.ktds.dsquare.common.mailing.MailService;
import com.ktds.dsquare.member.dto.request.*;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberSelectService memberSelectService;
    private final MemberService memberService;
    private final AccountService accountService;
    private final MailService mailService;


    @PostMapping("/account/signup/authenticate")
    public ResponseEntity<?> authenticateAccount(@RequestBody AccountAuthenticationRequest request) {
        accountService.authenticateAccount(request);
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/account/signup/authenticate")
    public ResponseEntity<?> validateAccountAuthentication(@RequestBody AccountAuthenticationValidationRequest request) {
        accountService.validateAccountAuthentication(request);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

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
     * 회원 존재 여부 확인 (중복 확인)
     */
    @PostMapping("/member/members/existings")
    public ResponseEntity<Boolean> checkMemberExistence(@RequestBody MemberExistenceCheckRequest request) {
        return new ResponseEntity<>(memberSelectService.checkMemberExistence(request), HttpStatus.OK);
    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping("/member/members/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequest request) {
        return new ResponseEntity<>(memberService.updateMember(id, request), HttpStatus.OK);
    }
    @PatchMapping("/member/members/{id}/profile/image")
    public ResponseEntity<?> updateMemberProfileImage(@PathVariable Long id, @RequestPart MultipartFile image, @AuthUser Member user) throws IOException {
        return new ResponseEntity<>(memberService.updateMember(id, image, user), HttpStatus.OK);
    }

    /**
     * 관리자용 회원 정보 수정
     */
    @PatchMapping("/admin/members/{id}")
    public ResponseEntity<?> updateMemberForAdmin(@PathVariable Long id, @RequestBody MemberUpdateRequestForAdmin request) {
        return new ResponseEntity<>(memberService.updateMemberForAdmin(id, request), HttpStatus.OK);
    }

    /**
     * 비밀번호 찾기
     */
    @PostMapping("/account/find-pw")
    public ResponseEntity<Void> findPassword(@RequestBody String to) {
        mailService.findPassword(to);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 비밀번호 변경
     */
    @PatchMapping({"/account/change-pw", "/account/reset-pw"})
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeRequest passwordChangeRequest) throws MemberException, IllegalArgumentException {
        memberService.changePassword(passwordChangeRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    * 회원 탈퇴
    * */
    @PatchMapping("/account/members/{id}")
    public ResponseEntity<?> withdrawMember(@PathVariable Long id, @AuthUser Member user) throws IOException {
        memberService.withdrawMember(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
