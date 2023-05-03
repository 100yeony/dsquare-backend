package com.ktds.dsquare.common.dashboard;

import com.ktds.dsquare.common.dashboard.dto.BestUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    //read - 질문왕 & 답변왕
    @GetMapping("/dashboard/best-users")
    public List<BestUserResponse> getAllBestUsers(@RequestParam String key) {
        return dashboardService.getAllBestUsers(key);
    }

}
