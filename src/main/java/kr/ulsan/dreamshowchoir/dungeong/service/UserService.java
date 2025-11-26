package kr.ulsan.dreamshowchoir.dungeong.service;

import jakarta.persistence.EntityNotFoundException;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.UserRepository;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.MemberProfileResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * OAuth 로그인 후, 추가 필수 정보(핸드폰, 생년월일 등)를 입력받아 저장한다.
     * (최초 가입 절차 - Onboarding)
     *
     * @param userId     현재 로그인한 사용자 ID
     * @param requestDto 추가 정보 DTO
     * @return 업데이트된 사용자 정보 DTO
     */
    public UserResponseDto updateSignUpInfo(Long userId, UserSignUpRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // User 엔티티의 updateAdditionalInfo 메서드 호출 (이전 단계에서 추가했어야 함)
        user.updateAdditionalInfo(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getBirthDate(),
                requestDto.getGender(),
                requestDto.getTermsAgreed()
        );

        // UserResponseDto 생성 및 반환
        // (기존 AuthService의 로직을 참고하여 Builder 사용)
        return UserResponseDto.builder()
                .user(user)
                .profile(user.getMemberProfile()) // 프로필이 없으면 null 처리됨
                .build();
    }

    /**
     * 공개된 단원 목록 조회
     * (홈페이지 '단원 소개' 페이지용)
     *
     * @return 단원 프로필 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<MemberProfileResponseDto> getPublicMembers() {
        return userRepository.findAllPublicMembers().stream()
                // User 객체에서 MemberProfile을 꺼내서 DTO 생성자에 전달
                .map(user -> new MemberProfileResponseDto(user.getMemberProfile()))
                .collect(Collectors.toList());
    }

    // 추후 회원 탈퇴(withdraw), 내 정보 수정(updateProfile) 등도 여기에 추가하면 됩니다.
}