package com.ktds.dsquare.board.carrot;

import com.ktds.dsquare.board.carrot.dto.CarrotRequest;
import com.ktds.dsquare.member.Member;
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
@Table(name = "COMM_CARROT")
public class Carrot {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrotWriter")
    private Member carrotWriter;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    @Column(nullable = false)
    private Long viewCnt;

    @Column(nullable = false)
    private Boolean deleteYn;


    public static Carrot toEntity(CarrotRequest dto,Member writer){
        LocalDateTime now = LocalDateTime.now();
        return Carrot.builder()
                .carrotWriter(writer)
                .title(dto.getTitle())
                .content(dto.getContent())
                .createDate(now)
                .viewCnt(0L)
                .deleteYn(false)
                .build();
    }

    public void updateCarrot(CarrotRequest dto){
        LocalDateTime now = LocalDateTime.now();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.lastUpdateDate = now;
    }

    public void deleteCarrot(){ this.deleteYn = true; }

    public void increaseViewCnt() {
        this.viewCnt += 1;
    }

}
