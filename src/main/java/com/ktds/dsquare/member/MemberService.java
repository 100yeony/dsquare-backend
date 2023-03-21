package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public BriefMemberInfo insertMember(SignupRequest signupRequest) throws Exception {
        Member member = Member.toEntity(signupRequest);
        Member savedMember = memberRepository.save(member);
        return BriefMemberInfo.toDto(savedMember);
    }

    public List<BriefMemberInfo> selectAllMember() {
        List<Member> members = memberRepository.findAll();
        return members.stream()
                .map(BriefMemberInfo::toDto)
                .collect(Collectors.toList());
    }

}
