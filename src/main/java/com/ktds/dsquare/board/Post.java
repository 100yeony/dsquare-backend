package com.ktds.dsquare.board;

import lombok.Getter;

import javax.persistence.*;

@Getter
@DiscriminatorColumn // (name = "DTYPE") // use default value
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_sequence")
    @SequenceGenerator(name = "post_sequence", sequenceName = "post_seq", initialValue = 100, allocationSize = 1)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member writer;

}

