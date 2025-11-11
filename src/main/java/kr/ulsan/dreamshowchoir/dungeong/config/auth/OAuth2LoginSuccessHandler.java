package kr.ulsan.dreamshowchoir.dungeong.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ulsan.dreamshowchoir.dungeong.config.jwt.JwtTokenProvider;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    // (TODO: 나중에 application.properties에서 프론트엔드 주소를 주입받도록 수정)
    private final String FRONTEND_REDIRECT_URL = "http://localhost:3000/auth/callback"; // 프론트엔드의 OAuth 콜백 주소

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Spring Security가 반환한 Principal(DefaultOidcUser 등)을 가져옴
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // Authentication 객체를 OAuth2AuthenticationToken으로 형변환
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // 공급자 ID(e.g., google, kakao)를 가져옴
        String providerId = oauthToken.getAuthorizedClientRegistrationId();

        // 주입받은 Repository에서 'ClientRegistration' 객체를 조회
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(providerId);

        // applcication.properties에 설정된 'userNameAttributeName' ("sub", "id", "response")을 동적으로 가져옴
        String userNameAttributeName = clientRegistration
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // userNameAttributeName을 전달
        OAuthAttributes attributes = OAuthAttributes.of(
                providerId,
                userNameAttributeName,
                oAuth2User.getAttributes()
        );

        // AuthService를 호출하여 DB에 유저 저장/업데이트
        User user = authService.loadOrRegisterUser(
                attributes.getProvider(),
                attributes.getOauthId(),
                attributes.getEmail(),
                attributes.getName(),
                attributes.getProfileImageKey()
        );

        // DB의 User 객체로 UserPrincipal 생성
        UserPrincipal userPrincipal = new UserPrincipal(user);

        // UserPrincipal로 새로운 Authentication 객체 생성 (JWT 생성용)
        Authentication authForToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                userPrincipal,
                null,
                userPrincipal.getAuthorities()
        );

        // 새로운 Authentication 객체로 JWT 토큰 생성
        String jwtToken = jwtTokenProvider.createToken(authForToken);

        // 토큰을 쿼리 파라미터에 담아 프론트엔드 URL로 리디렉션
        String targetUrl = UriComponentsBuilder.fromUriString(FRONTEND_REDIRECT_URL)
                .queryParam("token", jwtToken) // 토큰을 "token"이라는 이름의 쿼리 파라미터로 추가
                .build().toUriString();

        // 세션 기반이 아니므로, 기존 인증 관련 세션 데이터를 삭제
        clearAuthenticationAttributes(request);

        // 지정된 URL로 리디렉션
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}