package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.MemberUpdateRequest;
import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.dto.response.BriefMemberInfo;
import com.ktds.dsquare.member.dto.response.MemberInfo;
import com.ktds.dsquare.member.team.Team;
import com.ktds.dsquare.member.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                .orElseThrow(() -> new EntityNotFoundException("No such member with ID " + id));
        return MemberInfo.toDto(member);
    }

    public MemberInfo updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No such member with ID " + id));
        Team newTeam = teamRepository.findById(request.getTid())
                .orElseThrow(() -> new EntityNotFoundException("No such team with ID " + request.getTid()));

        member.update(request);
        member.join(newTeam);
        return MemberInfo.toDto(memberRepository.save(member));
    }

}
