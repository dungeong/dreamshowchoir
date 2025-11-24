package kr.ulsan.dreamshowchoir.dungeong.controller;

import jakarta.validation.Valid;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryListResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    /**
     * 갤러리 게시글 생성 (이미지/비디오 첨부 가능)
     * (MEMBER 이상 권한 필요)
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryResponseDto> createGallery(
            @Valid @RequestPart(value = "dto") GalleryCreateRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Long userId
    ) {
        GalleryResponseDto createdGallery = galleryService.createGallery(requestDto, files, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGallery);
    }

    /**
     * 갤러리 목록 조회 (페이징)
     * (전체 공개)
     */
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
    @GetMapping("/{galleryId}")
    public ResponseEntity<GalleryResponseDto> getGalleryDetail(@PathVariable Long galleryId) {
        GalleryResponseDto galleryDetail = galleryService.getGalleryDetail(galleryId);
        return ResponseEntity.ok(galleryDetail);
    }

    /**
     * 갤러리 게시글 수정 (미디어 추가/삭제 포함)
     * (MEMBER 이상 권한 필요)
     */
    @PatchMapping(value = "/{galleryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GalleryResponseDto> updateGallery(
            @PathVariable Long galleryId,
            @Valid @RequestPart(value = "dto") GalleryUpdateRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Long userId
    ) {
        GalleryResponseDto updatedGallery = galleryService.updateGallery(galleryId, requestDto, files, userId);
        return ResponseEntity.ok(updatedGallery);
    }

    /**
     * 갤러리 게시글 삭제 (논리 삭제)
     * (MEMBER 이상 권한 필요)
     */
    @DeleteMapping("/{galleryId}")
    public ResponseEntity<Void> deleteGallery(
            @PathVariable Long galleryId,
            @AuthenticationPrincipal Long userId
    ) {
        galleryService.deleteGallery(galleryId, userId);
        return ResponseEntity.noContent().build();
    }
}