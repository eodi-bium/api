package com.eodi.bium.member.service;

import static com.eodi.bium.global.error.ExceptionMessage.USER_NOT_FOUND;
import static java.util.stream.Collectors.toList;

import com.eodi.bium.draw.api.TrashRecordService;
import com.eodi.bium.draw.dto.response.TrashRecordResponse;

import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.member.dto.response.MemberInfoResponse;
import com.eodi.bium.member.dto.response.RecyclingRecord;
import com.eodi.bium.member.entity.Member;
import com.eodi.bium.member.mapper.MemberInfoResponseMapper;
import com.eodi.bium.member.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class MemberInfoService {

    private final MemberRepository memberRepository;
    private final TrashRecordService trashRecordService;

    public MemberInfoResponse getMemberInfo(String memberId) {
        // 멤버 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<TrashRecordResponse> trashRecordResponses = trashRecordService.getRecordsByMemberId(
            memberId);

        return MemberInfoResponseMapper.fromDrawResponse(member, trashRecordResponses);
    }
}