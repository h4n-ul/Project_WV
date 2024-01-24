package com.h4nul.stream.waves;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;


@Controller
@RequiredArgsConstructor
@RequestMapping("/backend")
public class TestController {
    @GetMapping("test")
    @ResponseBody
    public String getMethodName(HttpSession session) {
        return session.getId();
    }
    
}
