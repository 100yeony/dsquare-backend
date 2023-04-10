package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import com.ktds.dsquare.member.team.Team;
import com.ktds.dsquare.member.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    private final BCryptPasswordEncoder passwordEncoder;


    @Transactional
    public BriefMemberInfo insertMember(SignupRequest signupRequest) throws RuntimeException {
        signupRequest.setPw(passwordEncoder.encode(signupRequest.getPw()));

        Member member = Member.toEntity(signupRequest);
        Team team = teamRepository.findById(signupRequest.getTid())
                .orElseThrow(() -> new RuntimeException("No such team."));
        member.join(team);

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
