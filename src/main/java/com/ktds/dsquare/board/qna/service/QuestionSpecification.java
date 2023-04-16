package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Question;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;

public class QuestionSpecification {
    //업무 분류(workYn)
    public static Specification<Question> equalNotWork(Integer cid){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cid"), cid));
    }
    //삭제되지 않은 글(deleteYn=false)
    public static Specification<Question> equalNotDeleted(Boolean deleteYn){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleteYn"), false));
    }
    public static Specification<Question> notEqualNotWork(Integer cid){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get("cid"), cid));
    }
    //카테고리 검색(cid)
    public static Specification<Question> equalCid(Integer cid) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cid"), cid);
    }
    //사용자 검색(member)
    public static Specification<Question> equalWriterId(Long id) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("writerId"), id));
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

