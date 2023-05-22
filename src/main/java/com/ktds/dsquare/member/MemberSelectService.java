package com.ktds.dsquare.member;

import com.ktds.dsquare.member.dto.request.MemberExistenceCheckRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MemberSelectService {

    private final MemberRepository memberRepository;


    public static Specification<Member> searchWith(Map<String, String> params) {
        return ((root, query, builder) -> { // Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder
            List<Predicate> predicates = new ArrayList<>();
            if (params.containsKey("email")) {
                predicates.add(
                        builder.equal(root.get("email"), params.get("email"))
                );
            }
            if (params.containsKey("nickname")) {
                predicates.add(
                        builder.equal(root.get("nickname"), params.get("nickname"))
                );
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public boolean checkMemberExistence(MemberExistenceCheckRequest request) {
        List<Member> members = findMember(request.getType(), request.getValue());
        return members != null && members.size() > 0;
    }
    private List<Member> findMember(String key, String value) {
        if (!StringUtils.hasText(key) || !StringUtils.hasText(value))
            return Collections.emptyList();

        return memberRepository.findAll(searchWith(Map.of(key.toLowerCase(), value)));
    }

    public Member selectWithId(long id) {
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
