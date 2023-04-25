package com.ktds.dsquare.board.talk;

import com.ktds.dsquare.board.tag.TalkTag;
import com.ktds.dsquare.board.talk.dto.TalkRequest;
import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Talk {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;       // 기본값 0

    @Column(nullable = false)
    private Boolean deleteYn;       // 기본값 false

    @OneToMany(mappedBy = "talk")
    private List<TalkTag> TalkTags = new ArrayList<>();

    public static Talk toEntity(TalkRequest request, Member writer) {
        return Talk.builder()
                .writer(writer)
                .title(request.getTitle())
                .content(request.getContent())
                .createDate(LocalDateTime.now())
                .viewCnt(0L)
                .deleteYn(false)
                .build();
    }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }

    public void updateTalk(String title, String content) {
        this.title = title;
        this.content = content;
        this.lastUpdateDate = LocalDateTime.now();
    }

    public void deleteTalk(){
        this.deleteYn = true;
    }

}
