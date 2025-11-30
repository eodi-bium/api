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
    public ResponseEntity<?> getInfo() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        System.out.println("principal = " + principal);
        String memberId = "";

        // 2. Principal이 UserDetails 타입인지 확인 후 형변환
        if (principal instanceof UserDetails) {
            memberId = ((UserDetails) principal).getUsername(); // UserDetails의 username이 memberId입니다.
        } else {
            // principal이 문자열("anonymousUser")인 경우 등 예외 처리
            memberId = principal.toString();
        }
        return ResponseEntity.ok(memberInfoService.getMemberInfo(memberId));
    }
}
