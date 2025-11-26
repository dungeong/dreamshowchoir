package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.user.MemberProfileResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/info") // 공개 정보 API 공통 주소
@RequiredArgsConstructor
public class InfoController {

    private final UserService userService;

    /**
     * 단원 소개 목록을 조회하는 API (공개 프로필만)
     * (GET /api/info/members)
     * (전체 공개 - SecurityConfig에서 permitAll 필요)
     *
     * @return 정단원 목록 리스트 (민감정보 제외된 DTO)
     */
    @GetMapping("/members")
    public ResponseEntity<List<MemberProfileResponseDto>> getPublicMembers() {

        // Service를 호출하여 공개된 정단원 목록 조회
        List<MemberProfileResponseDto> members = userService.getPublicMembers();

        // 200 OK와 함께 리스트 반환
        return ResponseEntity.ok(members);
    }
}