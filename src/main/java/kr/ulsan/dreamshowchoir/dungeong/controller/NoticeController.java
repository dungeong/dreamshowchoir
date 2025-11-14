package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.*;
import kr.ulsan.dreamshowchoir.dungeong.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 공지사항 생성 API
     * (POST /api/notices)
     *
     * @param requestDto 공지사항 제목, 내용 (JSON)
     * @param userId     JWT 토큰에서 추출한 현재 로그인한 사용자의 ID
     * @return 생성된 공지사항의 상세 정보 (JSON)
     */
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice(
            @Valid @RequestBody NoticeCreateRequestDto requestDto,
            @AuthenticationPrincipal Long userId
    ) {

        // Service를 호출하여 공지사항 생성
        NoticeResponseDto createdNotice = noticeService.createNotice(requestDto, userId);

        // 201 Created 상태 코드와 함께 생성된 공지사항 정보 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
    }

    /**
     * 공지사항 목록 조회 API
     * (GET /api/notices?page=0&size=10&sort=createdAt,desc)
     *
     * @param pageable 쿼리 파라미터 (page, size, sort)
     * @return 페이징된 공지사항 목록 (JSON)
     */
    @GetMapping
    public ResponseEntity<PageResponseDto<NoticeListResponseDto>> getNoticeList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        PageResponseDto<NoticeListResponseDto> noticeList = noticeService.getNoticeList(pageable);

        return ResponseEntity.ok(noticeList);
    }

    /**
     * 공지사항 상세 조회 API
     * (GET /api/notices/{noticeId})
     *
     * @param noticeId URL 경로에서 추출한 공지사항 ID
     * @return 공지사항 상세 정보 (JSON)
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNoticeDetail(
            @PathVariable Long noticeId
    ) {
        NoticeResponseDto noticeDetail = noticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(noticeDetail);
    }

    /**
     * 공지사항 수정 API
     * (PATCH /api/notices/{noticeId})
     *
     * @param noticeId   수정할 공지사항 ID
     * @param requestDto 수정할 제목, 내용 (JSON)
     * @param userId     현재 로그인한 사용자 ID (Service에서 권한 검사용)
     * @return 수정된 공지사항 상세 정보 (JSON)
     */
    @PatchMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @PathVariable Long noticeId,
            @Valid @RequestBody NoticeUpdateRequestDto requestDto,
            @AuthenticationPrincipal Long userId
    ) {
        NoticeResponseDto updatedNotice = noticeService.updateNotice(noticeId, requestDto, userId);
        return ResponseEntity.ok(updatedNotice);
    }

    /**
     * 공지사항 삭제 API
     * (DELETE /api/notices/{noticeId})
     *
     * @param noticeId 삭제할 공지사항 ID
     * @param userId   현재 로그인한 사용자 ID (Service에서 권한 검사용)
     * @return 204 No Content
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable Long noticeId,
            @AuthenticationPrincipal Long userId
    ) {
        noticeService.deleteNotice(noticeId, userId);
        return ResponseEntity.noContent().build();
    }
}