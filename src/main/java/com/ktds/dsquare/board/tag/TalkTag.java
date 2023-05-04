package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.talk.Talk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "talk_tag")
public class TalkTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "talk_id")
    private Talk talk;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private LocalDateTime createDate;

    public static TalkTag toEntity(Talk talk, Tag tag) {
        return TalkTag.builder()
                .talk(talk)
                .tag(tag)
                .createDate(LocalDateTime.now())
                .build();
    }

}
