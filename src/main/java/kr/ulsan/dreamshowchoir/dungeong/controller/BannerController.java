package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.banner.BannerResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    /**
     * 메인 배너 목록 조회 API
     * (GET /api/banners)
     * (전체 공개)
     *
     * @return 활성화된 배너 목록 (JSON List)
     */
    @GetMapping
    public ResponseEntity<List<BannerResponseDto>> getBanners() {
        return ResponseEntity.ok(bannerService.getActiveBanners());
    }
}