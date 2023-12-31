package com.ktds.dsquare.board.talk;

import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.talk.dto.TalkRegisterRequest;
import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert @DynamicUpdate
@Table(name = "comm_talk")
public class Talk extends Post {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @Column(nullable = false, length = 100)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;       // 기본값 0

    @Column(nullable = false)
    private Boolean deleteYn;       // 기본값 false

    @OneToMany(mappedBy = "talk")
    private List<TalkTag> TalkTags;

    private Long likeCnt;

    public static Talk toEntity(TalkRegisterRequest request, Member writer) {
        LocalDateTime now = LocalDateTime.now();
        return Talk.builder()
                .writer(writer)
                .title(request.getTitle())
                .content(request.getContent())
                .createDate(now.plusHours(9))
                .viewCnt(0L)
                .deleteYn(false)
                .likeCnt(0L)
                .build();
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }

    public void updateTalk(String title, String content) {
        LocalDateTime now = LocalDateTime.now();
        this.title = title;
        this.content = content;
        this.lastUpdateDate = now.plusHours(9);
    }

    public void deleteTalk(){
        this.deleteYn = true;
    }

    public void like() { this.likeCnt += 1; }

    public void cancleLike(){ this.likeCnt -= 1; }

}
