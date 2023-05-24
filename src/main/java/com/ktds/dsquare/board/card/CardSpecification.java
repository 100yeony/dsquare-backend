package com.ktds.dsquare.board.card;

import org.springframework.data.jpa.domain.Specification;

public class CardSpecification {

    //[필수 1] 삭제되지 않은 글(deleteYn=false)
    public static Specification<Card> equalNotDeleted(Boolean deleteYn){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("deleteYn"), deleteYn));
    }

    //[옵션] 팀 검색(proj_team)
    public static Specification<Card> equalProjTeam(Long projTeam) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("projTeam"), projTeam));
    }

    //[필수 2] 카드 선정 여부 : selectedYn = true || false
    public static Specification<Card> isSelectedCard(boolean selectedYn) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("selectionYn"), selectedYn));
    }

}

