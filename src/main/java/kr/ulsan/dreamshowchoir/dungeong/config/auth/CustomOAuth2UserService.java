package kr.ulsan.dreamshowchoir.dungeong.config.auth;

import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AuthService authService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 처리 시작");

        // 기본 DefaultOAuth2UserService를 통해 OAuth2User 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 제공자 식별 (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 제공자의 PK가 되는 필드명 (예: google="sub", kakao="id", naver="response")
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuth2User의 속성들을 우리 서비스에 맞게 변환 (OAuthAttributes DTO 활용)
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // DB에서 사용자를 찾거나, 없으면 새로 등록 (AuthService 활용)
        //    (attributes에 담긴 정보로 DB 저장/업데이트가 수행됨)
        User user = authService.loadOrRegisterUser(
                attributes.getProvider(),
                attributes.getOauthId(),
                attributes.getEmail(),
                attributes.getName(),
                attributes.getProfileImageKey(),
                attributes.getPhoneNumber(),
                attributes.getBirthDate(),
                attributes.getGender()
        );

        log.info("OAuth2 로그인 성공. 사용자 정보: {}", user.getEmail());

        // UserPricipal 객체를 생성하여 반환
        return new UserPrincipal(user, oAuth2User.getAttributes());
    }
}