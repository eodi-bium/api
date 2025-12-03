package com.eodi.bium.member.service;

import static com.eodi.bium.global.error.ExceptionMessage.USER_NOT_FOUND;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.api.TrashRecordService;
import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.draw.dto.response.TrashRecordResponse;

import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.member.dto.response.MemberInfoResponse;
import com.eodi.bium.member.entity.Member;
import com.eodi.bium.member.mapper.MemberInfoResponseMapper;
import com.eodi.bium.member.repository.MemberRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class MemberInfoService {

    private final MemberRepository memberRepository;
    private final TrashRecordService trashRecordService;
    private final EventService eventService;

    public MemberInfoResponse getMemberInfo(String memberId) {
        // 멤버 조회
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Pageable eventPageable = PageRequest.of(0, 20);

        List<TrashRecordResponse> trashRecordResponses = trashRecordService.getRecordsByMemberId(
            memberId);

        Slice<EventRecord> eventRecords = eventService.getEventWithMemberId(memberId,
            eventPageable);
        return MemberInfoResponseMapper.fromDrawResponse(member, trashRecordResponses,
            eventRecords);
    }
}