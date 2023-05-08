package com.ktds.dsquare.common.dashboard;

import com.ktds.dsquare.board.qna.dto.BriefQuestionResponse;
import com.ktds.dsquare.board.tag.TagService;
import com.ktds.dsquare.common.annotation.AuthUser;
import com.ktds.dsquare.common.dashboard.dto.BestUserResponse;
import com.ktds.dsquare.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final TagService tagService;

    //명예의 전당 - 궁금해요(주간, 월간)
    @GetMapping("/dashboard/hall-of-fame")
    public List<BriefQuestionResponse> getAllBestQna(@RequestParam String key, @AuthUser Member user) {
        return dashboardService.getHallOfFame(key, user);
    }

    //read - 질문왕 & 답변왕
    @GetMapping("/dashboard/best-users")
    public List<BestUserResponse> getAllBestUsers(@RequestParam String key) {
        return dashboardService.getAllBestUsers(key);
    }

    // 핫토픽 키워드
    @GetMapping("/board/dashboard/top7-tags")
    public ResponseEntity<List<String>> selectTop7Tags() {
        return ResponseEntity.ok(tagService.selectTop7Tags());
    }

}
