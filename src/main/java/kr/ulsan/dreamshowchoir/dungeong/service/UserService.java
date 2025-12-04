package kr.ulsan.dreamshowchoir.dungeong.service;

import jakarta.persistence.EntityNotFoundException;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.MemberProfile;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.Role;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.UserRepository;
import kr.ulsan.dreamshowchoir.dungeong.dto.common.PageResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.MemberProfileResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserSignUpRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        // GUEST 등급 -> USER 등급으로 (가입 완료 처리)
        if (user.getRole() == Role.GUEST) {
            user.upgradeToUser();
        }

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
    public PageResponseDto<MemberProfileResponseDto> getPublicMembers(String part, Pageable pageable) {

        // Repository 호출 (part가 없으면 전체 조회)
        Page<User> userPage = userRepository.findPublicMembers(part, pageable);

        // Entity -> DTO 변환
        Page<MemberProfileResponseDto> dtoPage = userPage.map(user -> new MemberProfileResponseDto(user.getMemberProfile()));

        // 공통 페이징 DTO로 반환
        return new PageResponseDto<>(dtoPage);
    }

    /**
     * 내 정보 수정
     * (이름, 전화번호, 생년월일, 성별)
     */
    public UserResponseDto updateMyInfo(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // User 엔티티에 updateInfo 메서드 추가 필요 (아래 참고)
        user.updateInfo(
                requestDto.getName(),
                requestDto.getPhoneNumber(),
                requestDto.getBirthDate(),
                requestDto.getGender()
        );

        // 단원(MEMBER)이라면 프로필 정보도 수정
        if (user.getRole() == Role.MEMBER) {
            MemberProfile profile = user.getMemberProfile();

            // 만약 프로필이 없다면 생성 (방어 코드)
            if (profile == null) {
                profile = MemberProfile.builder()
                        .user(user)
                        .part(requestDto.getPart()) // 초기값
                        .build();
                // (Repository save 로직이 필요하거나 Cascade 설정을 믿어야 함)
                // 보통 가입 승인 시 프로필이 생기므로 여기서는 update만 호출
            }

            // 프로필 업데이트 (Entity에 편의 메서드 추가 필요)
            profile.updateProfileInfo(
                    requestDto.getPart(),
                    requestDto.getInterests(),
                    requestDto.getMyDream(),
                    requestDto.getHashTags()
            );
        }

        return UserResponseDto.builder()
                .user(user)
                .profile(user.getMemberProfile())
                .build();
    }

    /**
     * 회원 탈퇴 (논리 삭제)
     * - DB: Soft Delete
     * - OAuth: (선택사항) 연동 해제 로직이 필요하면 여기에 추가
     */
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + userId));

        // 탈퇴 시 연관된 MemberProfile 등은 Cascade 설정에 따라 처리됨.
        // User 엔티티에 @SQLDelete가 적용되어 있으므로 delete() 호출 시 Soft Delete 됨.
        userRepository.delete(user);
    }

    /**
     * [관리자용] 특정 단원의 프로필 공개 여부 변경
     */
    public void changeMemberVisibility(Long targetUserId, boolean isPublic) {
        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다: " + targetUserId));

        MemberProfile profile = user.getMemberProfile();
        if (profile == null) {
            throw new IllegalStateException("해당 유저는 단원 프로필이 존재하지 않습니다.");
        }

        profile.changeVisibility(isPublic);
    }
}