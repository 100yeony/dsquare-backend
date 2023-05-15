package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.enums.BoardType;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.tag.repository.*;
import com.ktds.dsquare.board.talk.Talk;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final QuestionTagRepository questionTagRepository;
    private final TalkTagRepository talkTagRepository;
    private final CarrotTagRepository carrotTagRepository;

    // 새 태그(키워드) 등록
    @Transactional
    public void insertNewTags(List<String> newTags, Object obj) {
        for (String name : newTags) {
            Tag tag = tagRepository.findByName(name);
            if (tag == null) {
                tag = Tag.toEntity(name);
                tagRepository.save(tag);
            }

            if (obj instanceof Question) {
                QuestionTag qt = QuestionTag.toEntity((Question) obj, tag);
                questionTagRepository.save(qt);
            } else if (obj instanceof Talk) {
                TalkTag tt = TalkTag.toEntity((Talk) obj, tag);
                talkTagRepository.save(tt);
            } else if (obj instanceof Carrot) {
                CarrotTag ct = CarrotTag.toEntity((Carrot) obj, tag);
                carrotTagRepository.save(ct);
            }
        }
    }
    @Transactional
    public List<Tag> registerTags(List<String> tags) {
        if (ObjectUtils.isEmpty(tags))
            return List.of();

        Set<Tag> existingTagList = trimExistings(tags);

        List<Tag> tagList = Tag.toEntityList(tags);
        List<Tag> savedTags = tagRepository.saveAll(tagList);

        savedTags.addAll(existingTagList);
        return savedTags;
    }
    public Set<Tag> trimExistings(List<String> tags) {
        Set<Tag> existings = new HashSet<>();
        for (String tag : tags) {
            Optional.ofNullable(tagRepository.findByName(tag))
                            .ifPresent(existings::add);
        }
        tags.removeAll(existings.stream()
                .map(Tag::getName)
                .collect(Collectors.toList()));
        return existings;
    }
    @Transactional
    public List<PostTag> registerTags(List<String> tags, Post post) {
        if (ObjectUtils.isEmpty(tags) || post == null) {
            return null;
        }

        Set<Tag> existings = trimExistings(tags);
        List<Tag> tagList = Tag.toEntityList(tags);
        tagList.addAll(existings);
        return linkPost(tagList, post);
    }
    @Transactional
    public List<PostTag> linkPost(List<Tag> tags, Post post) {
        if (ObjectUtils.isEmpty(tags) || post == null)
            return null;

        List<PostTag> relations = tags.stream()
                .map(tag -> PostTag.toEntity(post, tag))
                .collect(Collectors.toList());
        return postTagRepository.saveAll(relations);
    }

    // 태그-질문 간 연관관계 삭제
    @Transactional
    public void deleteTagRelation(Object obj, Tag tag) {
        if(obj instanceof Question){
            questionTagRepository.deleteByPostAndTag((Question) obj, tag);
        } else if (obj instanceof Talk) {
            talkTagRepository.deleteByTalkAndTag((Talk) obj, tag);
        } else if (obj instanceof Carrot) {
            carrotTagRepository.deleteByCarrotAndTag((Carrot) obj, tag);
        }
    }

    // 대시보드 - 핫토픽 키워드 조회
    public List<String> selectTop7Tags() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.plusHours(9).minusDays(7);

        List<WeeklyTagDTO> weeklyTags = new ArrayList<>();
        weeklyTags.addAll(carrotTagRepository.findTagsWithinLastWeek(weekAgo));
        weeklyTags.addAll(questionTagRepository.findTagsWithinLastWeek(weekAgo));
        weeklyTags.addAll(talkTagRepository.findTagsWithinLastWeek(weekAgo));

        // 최신순으로 먼저 정렬
        Collections.sort(weeklyTags, Comparator.comparing(o -> -((o.getCreateDate().toInstant(ZoneOffset.ofTotalSeconds(0)).toEpochMilli()))));

        // 태그별 사용횟수 카운트
        Map<Long, Long> tagCounts = new LinkedHashMap<>();
        for (WeeklyTagDTO tag : weeklyTags) {
            Long k = tag.getTagId();
            Long v = tag.getTagCount();
            tagCounts.put(k, tagCounts.getOrDefault(k, 0L) + v);
        }

        // 카운트 많은 순으로 정렬
        List<Map.Entry<Long, Long>> sortedTagCounts = new ArrayList<>(tagCounts.entrySet());
        sortedTagCounts.sort(Map.Entry.<Long, Long>comparingByValue().reversed());

        // 카운트가 최소 3번 이상인 태그 중 최대 7개만 가져오기
        List<Long> top7TagsId = sortedTagCounts.stream()
                .filter(entry -> entry.getValue() >= 3)
                .limit(7)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 태그 id를 이용해서 태그 이름 가져오기
        List<String> top7Tags = new ArrayList<>();
        for (Long tagId : top7TagsId) {
            Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag Not Found"));
            top7Tags.add(tag.getName());
        }

        return top7Tags;
    }
}
