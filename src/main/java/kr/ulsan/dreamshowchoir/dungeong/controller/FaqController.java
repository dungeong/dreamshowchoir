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
}