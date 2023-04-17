package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.member.Member;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.List;

public class QuestionSpecification {

    //삭제되지 않은 글(deleteYn=false)
    public static Specification<Question> equalNotDeleted(Boolean deleteYn){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleteYn"), false));
    }

    //업무 분류(workYn=true & cid != 2)
    public static Specification<Question> notEqualNotWork(Integer cid){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("cid"), cid));
    }

    //비업무(cid=2) & 카테고리 검색(cid)
    public static Specification<Question> equalCid(Integer cid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cid"), cid);
    }

    //사용자 검색(member) - 조건에 매칭되는 모든 사용자(ex.이름 2글자만 겹치는 경우)
    public static Specification<Question> inWriter(List<Member> writerIds) {
        return (((root, query, criteriaBuilder) -> criteriaBuilder.in(root.get("writer")).value(writerIds)));

    }

    //제목+내용 검색(titleAndContent)
    public static Specification<Question> equalTitleAndContentContaining(String value) {
        return (root, query, criteriaBuilder) -> {
            String pattern = "%" + value + "%";
            Predicate titleLike = criteriaBuilder.like(root.get("title"), pattern);
            Predicate contentLike = criteriaBuilder.like(root.get("content"), pattern);
            return criteriaBuilder.or(titleLike, contentLike);
        };
    }

}

