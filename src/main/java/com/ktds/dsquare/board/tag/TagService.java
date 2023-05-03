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
import java.util.List;

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
}
