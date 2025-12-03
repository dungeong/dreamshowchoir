package kr.ulsan.dreamshowchoir.dungeong.config.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${frontend.error-url}")
    private String frontendErrorUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException{

        log.warn("OAuth2 로그인 실패: {}", exception.getMessage());

        // 설정 파일에서 불러온 URL 문자열을 URI 객체로 파싱
        URI baseUri = URI.create(this.frontendErrorUrl);

        // UriComponentsBuilder.newInstance()를 사용하여 명시적으로 조립
        String targetUrl = UriComponentsBuilder.newInstance()
                .scheme(baseUri.getScheme()) // 예: http
                .host(baseUri.getHost())     // 예: localhost
                .port(baseUri.getPort())     // 예: 3000
                .path(baseUri.getPath())     // 예: /auth/error
                .queryParam("error", exception.getLocalizedMessage()) // 에러 메시지 파라미터 추가
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}