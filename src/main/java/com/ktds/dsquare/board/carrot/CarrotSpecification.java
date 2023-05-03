package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.member.Member;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class CarrotSpecification {

    //삭제되지 않은 글(deleteYn=false)
    public static Specification<Carrot> equalNotDeleted(Boolean deleteYn){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleteYn"), deleteYn));
    }

    //사용자 검색(member) - 조건에 매칭되는 모든 사용자(ex.이름 2글자만 겹치는 경우)
    public static Specification<Carrot> inWriter(List<Member> writerIds) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("writer")).value(writerIds)));

    }

    //제목+내용 검색(titleAndContent)
    public static Specification<Carrot> equalTitleAndContentContaining(String value) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + value + "%";
            Predicate titleLike = criteriaBuilder.like(root.get("title"), pattern);
            Predicate contentLike = criteriaBuilder.like(root.get("content"), pattern);
            return criteriaBuilder.or(titleLike, contentLike);
        };
    }

}

