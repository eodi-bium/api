package com.eodi.bium.member.controller;

import com.eodi.bium.member.service.MemberInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoService memberInfoService;

    @GetMapping("/memberInfo")
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String memberId = userDetails.getUsername();
        return ResponseEntity.ok(memberInfoService.getMemberInfo(memberId));
    }
}
