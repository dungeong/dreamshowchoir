package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.MemberProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/member/profile")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    /**
     * 단원 프로필 이미지 수정(업로드) API
     * (PATCH /api/member/profile/image)
     * (MEMBER 권한 필요 - SecurityConfig 설정)
     *
     * @param userId 로그인한 사용자 ID
     * @param file   업로드할 파일 (Key: "file")
     */
    @PatchMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Multipart 요청 처리
    public ResponseEntity<UserResponseDto> updateProfileImage(
            @AuthenticationPrincipal Long userId,
            @RequestPart(value = "file") MultipartFile file // 파일 데이터 받기
    ) {
        UserResponseDto updatedProfile = memberProfileService.updateProfileImage(userId, file);
        return ResponseEntity.ok(updatedProfile);
    }
}