package com.ktds.dsquare.member;

import com.ktds.dsquare.board.card.Card;
import com.ktds.dsquare.board.carrot.Carrot;
import com.ktds.dsquare.board.like.Like;
import com.ktds.dsquare.board.comment.Comment;
import com.ktds.dsquare.board.qna.domain.Answer;
import com.ktds.dsquare.board.qna.domain.Category;
import com.ktds.dsquare.board.qna.domain.Question;
import com.ktds.dsquare.board.talk.Talk;
import com.ktds.dsquare.member.dto.request.MemberUpdateRequest;
import com.ktds.dsquare.member.dto.request.SignupRequest;
import com.ktds.dsquare.member.team.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
@Slf4j
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String pw;
    @Column(unique = true, nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;

    private String ktMail;

    private Long activityScore;

    private LocalDateTime lastLoginDate;
    private LocalDateTime lastPwChangeDate;

    private String role;

    @OneToMany(mappedBy = "manager")
    private List<Category> cid;

    @OneToMany(mappedBy = "qid")
    private List<Question> questionsList;
    
    @OneToMany(mappedBy = "writer")
    private List<Answer> answerList;

    //카드 글 작성자
    @OneToMany(mappedBy = "cardWriter")
    private List<Card> cardList;

    //카드 주인
    @OneToMany(mappedBy = "cardOwner")
    private List<Card> ownCard;

    @OneToMany(mappedBy = "member")
    private List<Like> likePosts;

    @OneToMany(mappedBy = "writer")
    private List<Comment> commentList;

    @OneToMany(mappedBy = "originWriter")
    private List<Comment> originCommentList;

    @OneToMany(mappedBy = "writer")
    private List<Carrot> carrotList;

    @OneToMany(mappedBy = "writer")
    private List<Talk> TalkList;
    public List<String> getRole() {
        return List.of(role);
    }

    public void join(Team team) {
        this.team = team;
    }

    public boolean login(String password, PasswordEncoder passwordEncoder) {
//        if (!StringUtils.hasText(password)) // TODO changePassword() 빈 문자열 문제와 함께 해결 필요
//            return false;
        return passwordEncoder.matches(password, this.pw);
    }

    public void update(MemberUpdateRequest request) {
        if (request.getContact() != null)
            this.contact = request.getContact();
    }

    public void changePassword(String newPassword) {
        if (!StringUtils.hasText(newPassword)) { // TODO 빈 문자열 걸러지지 않음
            throw new IllegalArgumentException("New password must exist.");
        }

        this.pw = newPassword;
        this.lastPwChangeDate = LocalDateTime.now();
    }

    public static Member toEntity(SignupRequest dto) {
        return Member.builder()
                .email(dto.getEmail())
                .pw(dto.getPw())
                .nickname(dto.getNickname())
                .name(dto.getName())
                .contact(dto.getContact())
                .ktMail(dto.getKtMail())
                .activityScore(0L)
                .lastLoginDate(LocalDateTime.now())
                .lastPwChangeDate(LocalDateTime.now())
                .role("USER")
                .build();
    }

}
