package com.ktds.dsquare.board.tag;

import com.ktds.dsquare.board.carrot.Carrot;
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
@Table(name = "carrot_tag")
public class CarrotTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrot_id")
    private Carrot carrot;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private LocalDateTime createDate;

    public static CarrotTag toEntity(Carrot carrot, Tag tag) {
        return CarrotTag.builder()
                .carrot(carrot)
                .tag(tag)
                .createDate(LocalDateTime.now())
                .build();
    }

}
