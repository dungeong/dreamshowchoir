package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.FaqCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.FaqResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.FaqUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.service.FaqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    /**
     * FAQ 생성 API (ADMIN 전용)
     * (POST /api/faq)
     */
    @PostMapping
    public ResponseEntity<FaqResponseDto> createFaq(
            @Valid @RequestBody FaqCreateRequestDto requestDto
    ) {
        FaqResponseDto createdFaq = faqService.createFaq(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaq);
    }

    /**
     * FAQ 목록 전체 조회 API (전체 허용)
     * (GET /api/faq)
     */
    @GetMapping
    public ResponseEntity<List<FaqResponseDto>> getFaqList() {
        List<FaqResponseDto> faqList = faqService.getFaqList();
        return ResponseEntity.ok(faqList);
    }

    /**
     * FAQ 상세 조회 API (전체 허용)
     * (GET /api/faq/{faqId})
     */
    @GetMapping("/{faqId}")
    public ResponseEntity<FaqResponseDto> getFaqDetail(
            @PathVariable Long faqId
    ) {
        FaqResponseDto faqDetail = faqService.getFaqDetail(faqId);
        return ResponseEntity.ok(faqDetail);
    }

    /**
     * FAQ 수정 API (ADMIN 전용)
     * (PATCH /api/faq/{faqId})
     */
    @PatchMapping("/{faqId}")
    public ResponseEntity<FaqResponseDto> updateFaq(
            @PathVariable Long faqId,
            @Valid @RequestBody FaqUpdateRequestDto requestDto
    ) {
        FaqResponseDto updatedFaq = faqService.updateFaq(faqId, requestDto);
        return ResponseEntity.ok(updatedFaq);
    }

    /**
     * FAQ 삭제 API (ADMIN 전용)
     * (DELETE /api/faq/{faqId})
     */
    @DeleteMapping("/{faqId}")
    public ResponseEntity<Void> deleteFaq(
            @PathVariable Long faqId
    ) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.noContent().build();
    }
}