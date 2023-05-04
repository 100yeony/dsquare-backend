package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.tag.repository.CarrotTagRepository;
import com.ktds.dsquare.board.tag.repository.QuestionTagRepository;
import com.ktds.dsquare.board.tag.repository.TagRepository;
import com.ktds.dsquare.board.tag.repository.TalkTagRepository;
import com.ktds.dsquare.board.talk.Talk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
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

    // 태그-질문 간 연관관계 삭제
    @Transactional
    public void deleteTagRelation(Object obj, Tag tag) {
        if(obj instanceof Question){
            questionTagRepository.deleteByQuestionAndTag((Question) obj, tag);
        } else if (obj instanceof Talk) {
            talkTagRepository.deleteByTalkAndTag((Talk) obj, tag);
        } else if (obj instanceof Carrot) {
            carrotTagRepository.deleteByCarrotAndTag((Carrot) obj, tag);
        }
    }

    // 대시보드 - 핫토픽 키워드 조회
    public List<String> selectTop7Tags() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);

        List<Object[]> weeklyTags = new ArrayList<>();
        weeklyTags.addAll(carrotTagRepository.findTagsWithinLastWeek(weekAgo));
        weeklyTags.addAll(questionTagRepository.findTagsWithinLastWeek(weekAgo));
        weeklyTags.addAll(talkTagRepository.findTagsWithinLastWeek(weekAgo));

        // 최신순으로 먼저 정렬
        List<Object[]> sortedTags = weeklyTags.stream()
                .sorted(Comparator.comparingLong(o -> -((Timestamp) o[2]).getTime()))
                .collect(Collectors.toList());

        // 태그별 사용횟수 카운트
        Map<Integer, Integer> tagCounts = new LinkedHashMap<>();
        for (Object[] tag : sortedTags) {
            Integer k = Integer.parseInt(String.valueOf(tag[0]));
            Integer v = Integer.parseInt(String.valueOf(tag[1]));
            tagCounts.put(k, tagCounts.getOrDefault(k, 0) + v);
        }

        // 카운트 많은 순으로 정렬
        List<Map.Entry<Integer, Integer>> sortedTagCounts = new ArrayList<>(tagCounts.entrySet());
        sortedTagCounts.sort(Map.Entry.<Integer, Integer>comparingByValue().reversed());

        // 카운트가 최소 3번 이상인 태그 중 최대 7개만 가져오기
        List<Integer> top7TagsId = sortedTagCounts.stream()
                .filter(entry -> entry.getValue() >= 3)
                .limit(7)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 태그 id를 이용해서 태그 이름 가져오기
        List<String> top7Tags = new ArrayList<>();
        for (Integer tagId : top7TagsId) {
            String newTagName = tagRepository.findTagById(tagId.longValue()).getName();
            top7Tags.add(newTagName);
        }

        return top7Tags;
    }
}
