package kr.ulsan.dreamshowchoir.dungeong.controller;

import jakarta.validation.Valid;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserSignUpRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.service.AuthService;
import kr.ulsan.dreamshowchoir.dungeong.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // 공통 경로
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService; // 기존에 만들어둔 '내 정보 조회' 로직 활용
    private final UserService userService; // 새로 만든 '추가 정보 입력' 로직 활용

    /**
     * 내 정보 조회 API
     * (GET /api/users/me)
     * (USER 권한 필요)
     *
     * @param userId JWT 토큰에서 추출한 사용자 ID
     * @return 내 상세 정보 (UserResponseDto)
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal Long userId) {
        // 기존 AuthService에 만들어두신 getUserInfo() 메소드를 그대로 재사용합니다.
        UserResponseDto myInfo = authService.getUserInfo(userId);
        return ResponseEntity.ok(myInfo);
    }

    /**
     * 회원가입 마무리 (추가 정보 입력) API
     * (PATCH /api/users/sign-up)
     * (USER 권한 필요 - OAuth 로그인 직후 호출)
     *
     * @param requestDto 추가 입력 정보 (핸드폰, 생년월일, 약관동의 등)
     * @param userId     JWT 토큰에서 추출한 사용자 ID
     * @return 업데이트된 내 정보
     */
    @PatchMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(
            @Valid @RequestBody UserSignUpRequestDto requestDto,
            @AuthenticationPrincipal Long userId
    ) {
        // 새로 만든 UserService의 로직을 호출합니다.
        UserResponseDto updatedUser = userService.updateSignUpInfo(userId, requestDto);
        return ResponseEntity.ok(updatedUser);
    }
}