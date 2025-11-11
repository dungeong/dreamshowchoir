package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.UserResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 현재 로그인된 사용자의 정보를 반환하는 API
     * @param userId @AuthenticationPrincipal을 통해 Spring Security가 주입해주는 현재 사용자의 ID
     * @return UserResponseDto
     */
    @GetMapping("/me") // GET /api/auth/me 요청을 처리
    public ResponseEntity<UserResponseDto> getMyInfo(@AuthenticationPrincipal Long userId) {

        // userId가 null이면 로그인되지 않은 상태이므로, 401 Unauthorized 응답
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        // AuthService를 호출하여 사용자 정보를 DTO로 받아옴
        UserResponseDto userInfo = authService.getUserInfo(userId);

        // 200 OK 상태와 함께 사용자 정보를 응답
        return ResponseEntity.ok(userInfo);
    }
}