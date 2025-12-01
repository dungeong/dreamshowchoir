package kr.ulsan.dreamshowchoir.dungeong.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryListResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Gallery (갤러리)", description = "갤러리 관련 API")
@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;


    /**
     * 갤러리 목록 조회 (페이징)
     * (전체 공개)
     */
    @Operation(summary = "갤러리 목록 조회", description = "갤러리 게시글 목록을 페이징하여 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<GalleryListResponseDto>> getGalleryList(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<GalleryListResponseDto> galleryList = galleryService.getGalleryList(pageable);
        return ResponseEntity.ok(galleryList);
    }

    /**
     * 갤러리 상세 조회
     * (전체 공개)
     */
    @Operation(summary = "갤러리 상세 조회", description = "특정 갤러리 게시글의 상세 정보를 조회합니다.")
    @GetMapping("/{galleryId}")
    public ResponseEntity<GalleryResponseDto> getGalleryDetail(@PathVariable Long galleryId) {
        GalleryResponseDto galleryDetail = galleryService.getGalleryDetail(galleryId);
        return ResponseEntity.ok(galleryDetail);
    }


}