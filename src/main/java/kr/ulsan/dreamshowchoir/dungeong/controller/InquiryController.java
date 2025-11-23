package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.inquiry.InquiryCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.inquiry.InquiryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inquiry") // 1. 문의하기 API의 공통 주소
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    /**
     * 새로운 문의를 생성하는 API (비로그인 가능)
     * (POST /api/inquiry)
     */
    @PostMapping
    public ResponseEntity<InquiryResponseDto> createInquiry(
            @Valid @RequestBody InquiryCreateRequestDto requestDto
    ) {
        InquiryResponseDto createdInquiry = inquiryService.createInquiry(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInquiry);
    }
}