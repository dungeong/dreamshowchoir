package kr.ulsan.dreamshowchoir.dungeong.controller;

import jakarta.validation.Valid;
import kr.ulsan.dreamshowchoir.dungeong.dto.JoinApplicationResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.JoinStatusUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.PageResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin") // 1. 관리자 API의 공통 주소
@RequiredArgsConstructor
public class AdminController {

    private final JoinService joinService;
    // (TODO: 나중에 다른 관리자용 서비스도 주입)

    /**
     * (관리자용) '대기 중'인 가입 신청 목록을 조회하는 API
     * (GET /api/admin/join-applications)
     *
     * @param pageable 쿼리 파라미터 (page, size, sort)
     * @return 페이징된 신청서 목록 (JSON)
     */
    @GetMapping("/join-applications")
    public ResponseEntity<PageResponseDto<JoinApplicationResponseDto>> getPendingApplications(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        PageResponseDto<JoinApplicationResponseDto> applicationList = joinService.getPendingApplications(pageable);

        return ResponseEntity.ok(applicationList);
    }

    /**
     * (관리자용) 가입 신청을 '승인' 또는 '거절'하는 API
     * (PATCH /api/admin/join-applications/{joinId})
     *
     * @param joinId     신청서 ID
     * @param requestDto { "status": "APPROVED" } 또는 { "status": "REJECTED" }
     * @return 변경된 신청서 상세 정보 (JSON)
     */
    @PatchMapping("/join-applications/{joinId}")
    public ResponseEntity<JoinApplicationResponseDto> updateJoinApplicationStatus(
            @PathVariable Long joinId,
            @Valid @RequestBody JoinStatusUpdateRequestDto requestDto
    ) {
        JoinApplicationResponseDto updatedApplication = joinService.updateJoinApplicationStatus(joinId, requestDto);
        return ResponseEntity.ok(updatedApplication);
    }
}