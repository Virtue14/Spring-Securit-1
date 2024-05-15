package com.virtue.spring.security.controller;

import com.virtue.spring.security.dto.JoinDTO;
import com.virtue.spring.security.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class JoinController {

    private final JoinService joinService;

    @Autowired
    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @GetMapping("/join")
    public String joinP() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProcess(JoinDTO joinDTO) {
        System.out.println("회원가입 username = " + joinDTO.getUsername());

        joinService.JoinProcess(joinDTO);

        return "redirect:/login";
    }

}
