package kr.ulsan.dreamshowchoir.dungeong.controller;

import jakarta.validation.Valid;
import kr.ulsan.dreamshowchoir.dungeong.domain.donation.DonationStatus;
import kr.ulsan.dreamshowchoir.dungeong.dto.activity.ActivityMaterialCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.activity.ActivityMaterialResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.activity.ActivityMaterialUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.banner.BannerResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.banner.BannerUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.common.PageResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.common.StatusUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.content.SiteContentCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.content.SiteContentResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.content.SiteContentUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.donation.DonationResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.faq.FaqCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.faq.FaqResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.faq.FaqUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.gallery.GalleryUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.history.HistoryCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.history.HistoryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.history.HistoryUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.inquiry.InquiryReplyRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.inquiry.InquiryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.notice.NoticeCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.notice.NoticeResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.notice.NoticeUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.JoinApplicationResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.*;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JoinService joinService;
    private final DonationService donationService;
    private final InquiryService inquiryService;
    private final SiteContentService siteContentService;
    private final HistoryService historyService;
    private final FaqService faqService;
    private final NoticeService noticeService;
    private final GalleryService galleryService;
    private final ActivityMaterialService activityMaterialService;
    private final BannerService bannerService;

    // ---------------------------------- 가입 신청 ----------------------------------

    /**
     * (관리자용) '대기 중'인 가입 신청 목록을 조회하는 API
     * (GET /api/admin/join-applications)
     *
     * @param pageable 쿼리 파라미터 (page, size, sort)
     * @return 페이징된 신청서 목록 (JSON)
     */
    @GetMapping("/join-applications")
    public ResponseEntity<PageResponseDto<JoinApplicationResponseDto>> getPendingApplications(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)
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
            @Valid @RequestBody StatusUpdateRequestDto requestDto
    ) {
        JoinApplicationResponseDto updatedApplication = joinService.updateJoinApplicationStatus(joinId, requestDto);
        return ResponseEntity.ok(updatedApplication);
    }

    // ---------------------------------- 후원 ----------------------------------

    /**
     * (관리자용) 상태별 후원 목록을 조회하는 API
     * (GET /api/admin/donations?status=PENDING)
     *
     * @param status   조회할 상태 (PENDING, COMPLETED, FAILED)
     * @param pageable 쿼리 파라미터 (page, size, sort)
     * @return 페이징된 후원 목록 (JSON)
     */
    @GetMapping("/donations")
    public ResponseEntity<PageResponseDto<DonationResponseDto>> getDonationsByStatus(
            // 쿼리 파라미터로 status를 받음 (기본값 PENDING)
            @RequestParam(defaultValue = "PENDING") DonationStatus status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        PageResponseDto<DonationResponseDto> donationList = donationService.getDonationListByStatus(status, pageable);

        return ResponseEntity.ok(donationList);
    }

    /**
     * (관리자용) 후원 신청을 '완료' 또는 '실패'로 변경하는 API
     * (PATCH /api/admin/donations/{donationId})
     *
     * @param donationId 후원 신청 ID
     * @param requestDto { "status": "COMPLETED" } 또는 { "status": "FAILED" }
     * @return 변경된 후원 신청 상세 정보 (JSON)
     */
    @PatchMapping("/donations/{donationId}")
    public ResponseEntity<DonationResponseDto> updateDonationStatus(
            @PathVariable Long donationId,
            @Valid @RequestBody StatusUpdateRequestDto requestDto
    ) {
        DonationResponseDto updatedDonation = donationService.updateDonationStatus(donationId, requestDto);
        return ResponseEntity.ok(updatedDonation);
    }

    // ---------------------------------- 문의 ----------------------------------

    /**
     * (관리자용) 상태별 문의 목록을 조회하는 API
     * (GET /api/admin/inquiry?status=PENDING)
     * (ADMIN 권한 필요)
     */
    @GetMapping("/inquiry")
    public ResponseEntity<PageResponseDto<InquiryResponseDto>> getInquiriesByStatus(
            // 쿼리 파라미터로 status를 받음 (기본값 PENDING)
            @RequestParam(defaultValue = "PENDING") String status,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) // 7. 오래된 순(ASC) 정렬
            Pageable pageable
    ) {
        PageResponseDto<InquiryResponseDto> inquiryList = inquiryService.getInquiryListByStatus(status, pageable);
        return ResponseEntity.ok(inquiryList);
    }

    /**
     * (관리자용) 문의에 답변을 추가하는 API
     * (PATCH /api/admin/inquiry/{inquiryId})
     * (ADMIN 권한 필요)
     */
    @PatchMapping("/inquiry/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> replyToInquiry(
            @PathVariable Long inquiryId,
            @Valid @RequestBody InquiryReplyRequestDto requestDto
    ) {
        InquiryResponseDto updatedInquiry = inquiryService.replyToInquiry(inquiryId, requestDto);
        return ResponseEntity.ok(updatedInquiry);
    }

    // ------------------------- 통합 콘텐츠 -------------------------

    /**
     * (관리자용) 통합 콘텐츠 생성 API
     * (POST /api/admin/content)
     */
    @PostMapping("/content")
    public ResponseEntity<SiteContentResponseDto> createSiteContent(
            @Valid @RequestBody SiteContentCreateRequestDto requestDto
    ) {
        SiteContentResponseDto createdContent = siteContentService.createSiteContent(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdContent);
    }

    /**
     * (관리자용) 통합 콘텐츠 수정 API
     * (PATCH /api/admin/content/{contentKey})
     * (예 : /api/admin/content/RECRUIT_GUIDE)
     */
    @PatchMapping("/content/{contentKey}")
    public ResponseEntity<SiteContentResponseDto> updateSiteContent(
            @PathVariable String contentKey,
            @Valid @RequestBody SiteContentUpdateRequestDto requestDto
    ) {
        SiteContentResponseDto updatedContent = siteContentService.updateSiteContent(contentKey, requestDto);
        return ResponseEntity.ok(updatedContent);
    }

    /**
     * (관리자용) 통합 콘텐츠 삭제 API
     * (DELETE /api/admin/content/{contentKey})
     */
    @DeleteMapping("/content/{contentKey}")
    public ResponseEntity<Void> deleteSiteContent(
            @PathVariable String contentKey
    ) {
        siteContentService.deleteSiteContent(contentKey);
        return ResponseEntity.noContent().build();
    }

    // ------------------------------ 연혁 -----------------------------

    /**
     * (관리자용) 연혁 생성 API
     * (POST /api/admin/history)
     */
    @PostMapping("/history")
    public ResponseEntity<HistoryResponseDto> createHistory(
            @Valid @RequestBody HistoryCreateRequestDto requestDto
    ) {
        HistoryResponseDto createdHistory = historyService.createHistory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHistory);
    }

    /**
     * (관리자용) 연혁 수정 API
     * (PATCH /api/admin/history/{historyId})
     */
    @PatchMapping("/history/{historyId}")
    public ResponseEntity<HistoryResponseDto> updateHistory(
            @PathVariable Long historyId,
            @Valid @RequestBody HistoryUpdateRequestDto requestDto
    ) {
        HistoryResponseDto updatedHistory = historyService.updateHistory(historyId, requestDto);
        return ResponseEntity.ok(updatedHistory);
    }

    /**
     * (관리자용) 연혁 삭제 API
     * (DELETE /api/admin/history/{historyId})
     */
    @DeleteMapping("/history/{historyId}")
    public ResponseEntity<Void> deleteHistory(
            @PathVariable Long historyId
    ) {
        historyService.deleteHistory(historyId);
        return ResponseEntity.noContent().build();
    }

    // --------------------------- FAQ ----------------------------

    /**
     * (관리자용) FAQ 생성 API
     * (POST /api/admin/faq)
     */
    @PostMapping("/faq")
    public ResponseEntity<FaqResponseDto> createFaq(
            @Valid @RequestBody FaqCreateRequestDto requestDto
    ) {
        FaqResponseDto createdFaq = faqService.createFaq(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFaq);
    }

    /**
     * (관리자용) FAQ 수정 API
     * (PATCH /api/admin/faq/{faqId})
     */
    @PatchMapping("/faq/{faqId}")
    public ResponseEntity<FaqResponseDto> updateFaq(
            @PathVariable Long faqId,
            @Valid @RequestBody FaqUpdateRequestDto requestDto
    ) {
        FaqResponseDto updatedFaq = faqService.updateFaq(faqId, requestDto);
        return ResponseEntity.ok(updatedFaq);
    }

    /**
     * (관리자용) FAQ 삭제 API
     * (DELETE /api/admin/faq/{faqId})
     */
    @DeleteMapping("/faq/{faqId}")
    public ResponseEntity<Void> deleteFaq(
            @PathVariable Long faqId
    ) {
        faqService.deleteFaq(faqId);
        return ResponseEntity.noContent().build();
    }

    // -------------------------- 공지사항 -----------------------

    /**
     * (관리자용) 공지사항 생성 API
     * (POST /api/admin/notices)
     *
     * @param requestDto 공지사항 제목, 내용 (JSON)
     * @param userId     JWT 토큰에서 추출한 현재 로그인한 사용자의 ID
     * @return 생성된 공지사항의 상세 정보 (JSON)
     */
    @PostMapping("/notices")
    public ResponseEntity<NoticeResponseDto> createNotice(
            @Valid @RequestPart(value = "dto") NoticeCreateRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Long userId
    ) {

        // Service를 호출하여 공지사항 생성
        NoticeResponseDto createdNotice = noticeService.createNotice(requestDto, files, userId);

        // 201 Created 상태 코드와 함께 생성된 공지사항 정보 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotice);
    }

    /**
     * (관리자용) 공지사항 수정 API
     * (PATCH /api/admin/notices/{noticeId})
     *
     * @param noticeId   수정할 공지사항 ID
     * @param requestDto 수정할 제목, 내용 (JSON)
     * @param userId     현재 로그인한 사용자 ID (Service에서 권한 검사용)
     * @return 수정된 공지사항 상세 정보 (JSON)
     */
    @PatchMapping("/notices/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @PathVariable Long noticeId,
            @Valid @RequestBody NoticeUpdateRequestDto requestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Long userId
    ) {
        NoticeResponseDto updatedNotice = noticeService.updateNotice(noticeId, requestDto, files);
        return ResponseEntity.ok(updatedNotice);
    }

    /**
     * (관리자용) 공지사항 삭제 API
     * (DELETE /api/admin/notices/{noticeId})
     *
     * @param noticeId 삭제할 공지사항 ID
     * @param userId   현재 로그인한 사용자 ID (Service에서 권한 검사용)
     * @return 204 No Content
     */
    @DeleteMapping("/notices/{noticeId}")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable Long noticeId,
            @AuthenticationPrincipal Long userId
    ) {
        noticeService.deleteNotice(noticeId, userId);
        return ResponseEntity.noContent().build();
    }

    // -------------------------- 갤러리 --------------------------

    /**
     * (관리자용) 갤러리 게시글 생성 (이미지/비디오 첨부 가능)
     * (POST /api/admin/gallery)
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
     * (관리자용) 갤러리 게시글 수정 (미디어 추가/삭제 포함)
     * (PATCH /api/admin/gallery)
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
     * (관리자용) 갤러리 게시글 삭제 (논리 삭제)
     * (DELETE /api/admin/gallery)
     */
    @DeleteMapping("/{galleryId}")
    public ResponseEntity<Void> deleteGallery(
            @PathVariable Long galleryId,
            @AuthenticationPrincipal Long userId
    ) {
        galleryService.deleteGallery(galleryId, userId);
        return ResponseEntity.noContent().build();
    }

    // -------------------------- 활동자료 관리 --------------------------


    /**
     * (관리자용) 활동자료 등록 API
     * (POST /api/admin/activity-materials)
     *
     * @param requestDto 자료 제목, 내용 DTO (JSON)
     * @param file       업로드할 파일 (MultipartFile)
     * @param userId     JWT 토큰에서 추출한 관리자 ID
     * @return 생성된 활동자료 상세 정보 (JSON)
     */
    @PostMapping(value = "/activity-materials", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ActivityMaterialResponseDto> createMaterial(
            @Valid @RequestPart(value = "dto") ActivityMaterialCreateRequestDto requestDto,
            @RequestPart(value = "file") MultipartFile file,
            @AuthenticationPrincipal Long userId
    ) {

        // Service를 호출하여 활동자료 생성
        ActivityMaterialResponseDto createdMaterial = activityMaterialService.createMaterial(requestDto, file, userId);

        // 201 Created 상태 코드와 함께 생성된 자료 정보 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMaterial);
    }

    /**
     * (관리자용) 활동자료 내용을 수정하는 API (파일 수정 불가, 텍스트만 수정)
     * (PATCH /api/admin/activity-materials/{materialId})
     *
     * @param materialId URL 경로에서 추출한 자료 ID
     * @param requestDto 수정할 제목, 내용 DTO (JSON)
     * @return 수정된 활동자료 상세 정보 (JSON)
     */
    @PatchMapping("/activity-materials/{materialId}")
    public ResponseEntity<ActivityMaterialResponseDto> updateMaterial(
            @PathVariable Long materialId,
            @Valid @RequestBody ActivityMaterialUpdateRequestDto requestDto
    ) {

        // Service를 호출하여 활동자료 내용 수정 (수정된 DTO 반환)
        ActivityMaterialResponseDto updatedMaterial = activityMaterialService.updateMaterial(materialId, requestDto);

        // 200 OK 상태와 함께 수정된 자료 정보 반환
        return ResponseEntity.ok(updatedMaterial);
    }

    /**
     * (관리자용) 활동자료를 삭제하는 API
     * (DELETE /api/admin/activity-materials/{materialId})
     *
     * @param materialId URL 경로에서 추출한 자료 ID
     * @return 204 No Content
     */
    @DeleteMapping("/activity-materials/{materialId}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long materialId) {

        // Service를 호출하여 활동자료 삭제
        activityMaterialService.deleteMaterial(materialId);

        // 204 No Content: 성공적으로 삭제되었으며, 반환할 본문(Body)이 없음
        return ResponseEntity.noContent().build();
    }

    // --------------------- 배너 ----------------------

    /**
     * (관리자용) 배너 등록 API
     * (POST /api/admin/banners)
     *
     * @param title       배너 제목
     * @param description 배너 설명 (선택)
     * @param file        배너 이미지 파일
     * @return 생성된 배너 정보 (JSON)
     */
    @PostMapping(value = "/banners", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponseDto> createBanner(
            @RequestPart(value = "title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "file") MultipartFile file
    ) {
        BannerResponseDto createdBanner = bannerService.createBanner(title, description, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBanner);
    }

    /**
     * (관리자용) 배너 수정 API (정보, 순서, 활성여부, 이미지 교체)
     * (PATCH /api/admin/banners/{bannerId})
     *
     * @param bannerId   수정할 배너 ID
     * @param requestDto 수정할 정보 (JSON)
     * @param file       교체할 파일 (선택 사항)
     * @return 수정된 배너 정보
     */
    @PatchMapping(value = "/banners/{bannerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BannerResponseDto> updateBanner(
            @PathVariable Long bannerId,
            @Valid @RequestPart(value = "dto") BannerUpdateRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file // 필수 아님
    ) {
        BannerResponseDto updatedBanner = bannerService.updateBanner(bannerId, requestDto, file);
        return ResponseEntity.ok(updatedBanner);
    }

    /**
     * (관리자용) 배너 삭제 API
     * (DELETE /api/admin/banners/{bannerId})
     *
     * @param bannerId 삭제할 배너 ID
     * @return 204 No Content
     */
    @DeleteMapping("/banners/{bannerId}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long bannerId) {
        bannerService.deleteBanner(bannerId);
        return ResponseEntity.noContent().build();
    }
}