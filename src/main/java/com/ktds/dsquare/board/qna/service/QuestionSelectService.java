package com.ktds.dsquare.board.qna.service;

import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import com.ktds.dsquare.board.comment.CommentRepository;
import com.ktds.dsquare.board.like.LikeRepository;
import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.qna.dto.response.BriefQuestionInfo;
import com.ktds.dsquare.board.qna.repository.AnswerRepository;
import com.ktds.dsquare.board.qna.repository.QuestionRepository;
import com.ktds.dsquare.member.Member;
import com.ktds.dsquare.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class QuestionSelectService {

    private final CategoryService categoryService;

    private final QuestionRepository questionRepository;
    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;


    // TODO to be deleted ("order" is useless. use "sort" in Pageable instead.)
    private Pageable createPageable(Pageable pageable, String order) {
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
    public List<BriefQuestionInfo> getQuestions(Map<String, String> params, Pageable pageable, Member user) {
        Page<Question> questions = questionRepository.findAll(
                searchWith(params), createPageable(pageable, params.get("order"))
        );
        return questions.stream()
                .map(question -> createBriefResponse(question, user))
                .collect(Collectors.toList());
    }
    public Specification<Question> searchWith(Map<String, String> params) {
        return ((root, query, builder) -> { // Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder
            List<Predicate> predicates = new ArrayList<>();

            // Default search condition
            predicates.add(builder.equal(root.get("deleteYn"), false));

            // Category-based search
            predicates.add(
                    determinePredicateForCategoryBasedSearch(root, builder, params)
            );
            // Key-Value-based search
            if (params.containsKey("key")) {
                predicates.add(
                        determinePredicateForKeyValueBasedSearch(root, builder, params)
                );
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private Predicate determinePredicateForCategoryBasedSearch(Root<Question> root, CriteriaBuilder builder, Map<String, String> params) {
        List<Category> categories = determineCategoryToSearch(params);
        return builder.in(root.get("category")).value(categories);
    }
    private List<Category> determineCategoryToSearch(Map<String, String> params) {
        // TODO seems to be that workYn doesn't need --> integrate to cid only
        boolean isAboutWork = Boolean.parseBoolean(params.get("workYn"));
        if (isAboutWork)
            return categoryService.getWorkCategories();

        if (params.containsKey("cid")) {
            int cid = Integer.parseInt(params.get("cid"));
            try {
                return categoryService.getLowerCategories( categoryService.getCategory(cid));
            } catch (Exception e) {
                // proceed with the next procedure
            }
        }

        return categoryService.getNonWorkCategories();
    }

    private Predicate determinePredicateForKeyValueBasedSearch(Root<Question> root, CriteriaBuilder builder, Map<String, String> params) {
        Assert.isTrue(params.containsKey("key"), "Searching key must exist.");

        String value = params.getOrDefault("value", "");
        switch (params.get("key").strip().toLowerCase()) {
            case "member":
                List<Member> members = memberRepository.findByNameContaining(value);
                return builder.in(root.get("writer")).value(members);
            case "titleandcontent":
                value = "%"+value+"%";
                return builder.or(
                        builder.like(root.get("title"), value),
                        builder.like(root.get("content"), value)
                );
            default:
                throw new IllegalArgumentException();
        }
    }

    public BriefQuestionInfo createBriefResponse(Question question, Member user) {
        long commentCnt, answerCnt;
        boolean isManagerAnswered, isLiked;

        commentCnt = commentRepository.countByPostId(question.getId());
        answerCnt = answerRepository.countByQuestion(question);

        isManagerAnswered = isManagerAnswered(question);
        isLiked = likeRepository.existsByPostIdAndMember(question.getId(), user);

        return BriefQuestionInfo.toDto(
                question,
                commentCnt,
                answerCnt,
                isManagerAnswered,
                isLiked
        );
    }

    public boolean isManagerAnswered(Question question) {
        Category category = question.getCategory();
        Member manager = category.getManager();
        return answerRepository.existsByWriter(manager);
    }

}
