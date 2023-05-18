package com.ktds.dsquare.member;

import com.ktds.dsquare.common.exception.BadLoginException;
import com.ktds.dsquare.common.exception.MemberException;
import com.ktds.dsquare.common.exception.MemberNotFoundException;
import com.ktds.dsquare.common.exception.UserNotFoundException;
import com.ktds.dsquare.common.file.FileService;
import com.ktds.dsquare.common.file.dto.FileSavedDto;
import com.ktds.dsquare.member.dto.request.MemberUpdateRequest;
import com.ktds.dsquare.member.dto.request.MemberUpdateRequestForAdmin;
import com.ktds.dsquare.member.dto.request.PasswordChangeRequest;
import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import com.ktds.dsquare.member.team.Team;
import com.ktds.dsquare.member.team.TeamRepository;
import com.ktds.dsquare.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

//    private final AttachmentService attachmentService;
    private final FileService fileService;

    private final BCryptPasswordEncoder passwordEncoder;
    private final WithdrawnMemberRepository withdrawnMemberRepository;


    @Transactional
    public BriefMemberInfo insertMember(SignupRequest signupRequest) throws RuntimeException {
        signupRequest.encodePassword(passwordEncoder);

        Member member = Member.toEntity(signupRequest);
        Team team = teamRepository.findById(signupRequest.getTid())
                .orElseThrow(() -> new RuntimeException("No such team."));
        member.join(team);

        Member savedMember = memberRepository.save(member);
        return BriefMemberInfo.toDto(savedMember);
    }

    public List<BriefMemberInfo> selectAllMembers(Map<String, String> params) {
        List<Member> members = memberRepository.findAll(searchWith(params));
        return members.stream()
                .map(BriefMemberInfo::toDto)
                .collect(Collectors.toList());
    }
    private static Specification<Member> searchWith(Map<String, String> params) {
        return ((root, query, builder) -> { // Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("email"))
                predicates.add(
                        builder.equal(root.get("email"), params.get("email"))
                );

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public MemberInfo selectMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such member with ID " + id));
        return MemberInfo.toDto(member);
    }

    // TODO add missing annotations
    // TODO consider another fit exception
    @Transactional
    public MemberInfo updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such member with ID " + id));
        if(request.getTid() != null) {
            Team newTeam = teamRepository.findById(request.getTid())
                    .orElseThrow(() -> new EntityNotFoundException("No such team with ID " + request.getTid()));
            member.join(newTeam);
        }

        if (!isAvailableNickname(request, member))
            throw new RuntimeException("Please check nickname.");

        member.update(request);
        return MemberInfo.toDto(member);
    }
    private boolean isAvailableNickname(MemberUpdateRequest request, Member member) {
        return StringUtils.hasText(request.getNickname()) &&
                (Objects.equals(member.getNickname(), request.getNickname())
                || !memberRepository.existsByNickname(request.getNickname()));
    }

    @Transactional
    public FileSavedDto updateMember(Long id, MultipartFile image, Member user) throws IOException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such member with ID " + id));
        Assert.state(member == user, "Illegal resource access");

        return updateProfileImage(member, image);
    }
    private FileSavedDto updateProfileImage(Member member, MultipartFile image) throws IOException {
        FileSavedDto savedProfileImage = uploadProfileImage(member, image);
        member.updateProfileImage(savedProfileImage.getUrl());
        return savedProfileImage;
    }
    private FileSavedDto uploadProfileImage(Member member, MultipartFile image) throws IOException {
        if (!ObjectUtils.isEmpty(member.getProfileImage()))
            log.debug("There is existing profile image.");
//            attachmentService.deleteAttachmentByPostDeletion(); // TODO Post <> Attachment 와 묶어 함께 생각할 필요 추가
        return fileService.uploadFile(image);
    }
    @Transactional
    public MemberInfo updateMemberForAdmin(Long id, MemberUpdateRequestForAdmin request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("No such member with ID " + id));
        member.update(request.getRole());
        return MemberInfo.toDto(member);
    }

    @Transactional
    public void withdrawMember(Long id, Member user) {
        Member member = memberRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("No such member with ID " + id));
        if(member.getWithdrawDate() != null)
            throw new UserNotFoundException("Users who have already withdrawn.");
//        권한 체크
        if(! (user.getRole().contains(Role.ADMIN) || user.getId() == id) ) {
            throw new RuntimeException("Not Authorized.");
        }
//        대체할 닉네임 생성 및 기존 정보 수정
        String nickname = createNewNickname();
        member.withdraw(nickname);
        // role 삭제

//        id를 회원탈퇴 테이블에 삽입
        WithdrawnMember withdrawnMember = WithdrawnMember.toEntity(id);
        withdrawnMemberRepository.save(withdrawnMember);
    }

    public String createNewNickname() {
        String code;
        Set<String> nicknameSet = memberRepository.findAll().stream()
                .map(Member::getNickname)
                .collect(Collectors.toSet());
        int limit = 10;
        do {
            code = RandomUtil.generateRandomNumber(8);
            System.out.println(code);
        } while (limit-- > 0 && nicknameSet.contains("탈퇴한 회원_"+code));
        return "탈퇴한 회원_"+code;
    }

    @Transactional
    public void findPassword(String email, String tempPassword) {
        try {
            Member member = memberRepository.findByEmail(email).orElse(null);
            if (member == null)
                return;

            member.changePassword(passwordEncoder.encode(tempPassword));
        } catch (Exception e) {
            log.warn("Error while finding password.");
            throw e;
        }
    }

    @Transactional
    public void changePassword(PasswordChangeRequest passwordChangeRequest) throws MemberException, IllegalArgumentException {
        Member member = memberRepository.findByEmail(passwordChangeRequest.getEmail())
                .orElseThrow(() -> new MemberNotFoundException(passwordChangeRequest.getEmail()));

        passwordChangeRequest.encodePassword(passwordEncoder);

        if (!member.login(passwordChangeRequest.getOriginalPassword(), passwordEncoder))
            throw new BadLoginException();

        member.changePassword(passwordChangeRequest.getChangedPassword());
    }


}
