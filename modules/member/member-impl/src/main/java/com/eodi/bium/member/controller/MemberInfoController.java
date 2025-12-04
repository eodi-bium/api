package com.eodi.bium.member.controller;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.member.service.MemberInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
    private final EventService eventService;

    @GetMapping("/memberInfo")
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String memberId = userDetails.getUsername();
        return ResponseEntity.ok(memberInfoService.getMemberInfo(memberId));
    }

    @GetMapping("/memberEvents")
    public ResponseEntity<Slice<EventRecord>> getMemberEvents(
        @AuthenticationPrincipal UserDetails userDetails,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        String memberId = userDetails.getUsername();
        return ResponseEntity.ok(eventService.getEventWithMemberId(memberId, pageable));
    }
}
