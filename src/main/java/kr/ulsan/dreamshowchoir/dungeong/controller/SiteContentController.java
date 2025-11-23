package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.content.SiteContentResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.SiteContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class SiteContentController {

    private final SiteContentService siteContentService;

    /**
     * 콘텐츠 조회 API (전체 공개)
     * (GET /api/content/{contentKey})
     * (예: /api/content/RECRUIT_GUIDE)
     */
    @GetMapping("/{contentKey}")
    public ResponseEntity<SiteContentResponseDto> getSiteContent(
            @PathVariable String contentKey
    ) {
        SiteContentResponseDto content = siteContentService.getSiteContent(contentKey);
        return ResponseEntity.ok(content);
    }

}