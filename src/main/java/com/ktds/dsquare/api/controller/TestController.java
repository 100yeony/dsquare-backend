package com.ktds.dsquare.api.controller;

import com.ktds.dsquare.common.exception.DSquareRuntimeException;
import com.ktds.dsquare.common.exception.StatusEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public Object test(){

        Map<String,Object> result = new HashMap<>();
        result.put("data", "test success");
        return  result;
    }

    @GetMapping("/testFail")
    public void testFail(){
       throw new DSquareRuntimeException(StatusEnum.TEST_FAIL.getCode(), StatusEnum.TEST_FAIL.getMessage());
    }
}
