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


}