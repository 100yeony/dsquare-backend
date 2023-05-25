package com.ktds.dsquare.board.card.service;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.board.card.CardRepository;
import com.ktds.dsquare.board.card.dto.CardInfo;
import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.common.exception.PostNotFoundException;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.team.Team;
import com.ktds.dsquare.member.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CardSelectService {

    private final CardRepository cardRepository;
    private final TeamRepository teamRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;


    public List<CardInfo> getCards(Map<String, String> params, Pageable pageable, Member user) {
        Page<Card> cards = cardRepository.findAll(searchWith(params), createPageable(pageable, params.get("order")));
        return cards.stream()
                .map(card -> createInfo(card, user))
                .collect(Collectors.toList());
    }

    public Specification<Card> searchWith(Map<String, String> params) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.isFalse(root.get("deleteYn")));

            params.putIfAbsent("isSelected", String.valueOf(false));
            predicates.add(builder.equal(root.get("selectionYn"), Boolean.valueOf(params.get("isSelected"))));

            // Search condition
            if (params.containsKey("projTeamId")) {
                Team team = teamRepository.findById(Long.valueOf(params.get("projTeamId")))
                        .orElseThrow(() -> new EntityNotFoundException("team not found"));
                predicates.add(builder.equal(root.get("projTeam"), team));
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        });
    }
    public Pageable createPageable(Pageable pageable, String order) { // TODO code duplication (same with QuestionSelectService#createPageable)
        Assert.notNull(pageable, "Pageable must exist.");

        if (!StringUtils.hasText(order))
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createDate").descending());

        Sort sort;
        switch (order.strip().toLowerCase()) {
            case "like":
                sort = Sort.by("likeCnt").descending();
                break;
            case "create":
            default:
                sort = Sort.by("createDate").descending();
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    public CardInfo createInfo(Card card, Member user) {
        long commentCnt = commentRepository.countByPostId(card.getId());
        boolean likeYn = likeRepository.existsByPostIdAndMember(card.getId(), user);

        return CardInfo.toDto(card, commentCnt, likeYn);
    }

    public Card selectWithId(long id) {
        return cardRepository.findById(id)
                .orElseThrow(()->new PostNotFoundException("card not found. Card ID: " + id));
    }

    @Transactional
    public CardInfo getCard(long id, Member user) {
        Card card = selectWithId(id);
        card.increaseViewCnt();
        return createInfo(card, user);
    }

}
