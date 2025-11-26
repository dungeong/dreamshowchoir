package kr.ulsan.dreamshowchoir.dungeong.service;

import jakarta.persistence.EntityNotFoundException;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.MemberProfile;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.Role;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.MemberProfileRepository;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.UserRepository;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어줌 (DI)
public class AuthService {

    private final UserRepository userRepository;
    private final MemberProfileRepository memberProfileRepository;

    /**
     * OAuth2 로그인 시, DB에 사용자가 없으면 회원가입, 있으면 정보를 업데이트한다.
     *
     * @param provider        OAuth 제공자 (e.g., "google", "kakao")
     * @param oauthId         제공자의 식별 ID
     * @param email           이메일
     * @param name            이름
     * @param profileImageKey 프로필 이미지 URL (S3 키)
     * @return 로그인한 사용자의 DTO
     */
    @Transactional
    public User loadOrRegisterUser(String provider, String oauthId, String email, String name, String profileImageKey) {

        // DB에서 OAuth 정보로 사용자를 찾음
        Optional<User> optionalUser = userRepository.findByOauthProviderAndOauthId(provider, oauthId);

        User user;
        if (optionalUser.isPresent()) {     // 이미 가입된 사용자인 경우 (로그인)
            user = optionalUser.get();
            // OAuth 프로필 정보가 변경되었을 수 있으니, 이름과 프로필 사진을 업데이트함
            user.updateOAuthInfo(name, profileImageKey);
            // @Transactional 덕분에, save()를 호출하지 않아도 더티 체킹으로 DB에 자동 반영됨
        } else {        // 처음 방문한 사용자인 경우 (자동 회원가입)
            user = User.builder()
                    .oauthProvider(provider)
                    .oauthId(oauthId)
                    .email(email)
                    .name(name)
                    .profileImageKey(profileImageKey)
                    .role(Role.USER) // 가입 시 기본 권한은 'USER'(일반 사용자)
                    .build();
            user = userRepository.save(user); // DB에 저장
        }

        return user;
    }

    /**
     * 현재 로그인 된 사용자의 ID로 상세 정보를 조회
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserInfo(Long userId) {

        // User 조회 (없으면 예외 발생)
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없습니다. ID : " + userId));

        // MemberProfile 조회 (없으면 null)
        MemberProfile profile = memberProfileRepository.findById(userId).orElse(null);

        // DTO로 변환하여 반환
        return UserResponseDto.builder()
                .user(user)
                .profile(profile)
                .build();
    }

    // (TODO: 여기에 추후 가입 승인(approveMember), 유저 정보 수정 등의 서비스 메소드가 추가될 수 있다.)
}