package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.board.Post;
import com.ktds.dsquare.board.carrot.dto.CarrotRegisterRequest;
import com.ktds.dsquare.board.tag.CarrotTag;
import com.ktds.dsquare.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "COMM_CARROT")
public class Carrot extends Post {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private Member writer;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;

    @Column(nullable = false)
    private Boolean deleteYn;

    @OneToMany(mappedBy = "carrot")
    private List<CarrotTag> carrotTags;

    private Long likeCnt;


    public static Carrot toEntity(CarrotRegisterRequest dto, Member writer){
        LocalDateTime now = LocalDateTime.now();
        return Carrot.builder()
                .writer(writer)
                .title(dto.getTitle())
                .content(dto.getContent())
                .createDate(now)
                .viewCnt(0L)
                .deleteYn(false)
                .likeCnt(0L)
                .build();
    }

    public void updateCarrot(CarrotRegisterRequest dto){
        LocalDateTime now = LocalDateTime.now();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.lastUpdateDate = now;
    }

    public void deleteCarrot(){ this.deleteYn = true; }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }

    public void like() { this.likeCnt += 1; }

    public void cancleLike(){ this.likeCnt -= 1; }
}
