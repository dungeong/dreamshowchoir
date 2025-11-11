package kr.ulsan.dreamshowchoir.dungeong.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // (TODO: 나중에 application.properties에서 프론트엔드 주소를 주입받도록 수정)
    private final String FRONTEND_ERROR_URL = "http://localhost:3000/auth/error"; // 프론트엔드의 에러 페이지

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.warn("OAuth2 로그인 실패: {}", exception.getMessage());

        // 에러 메시지를 쿼리 파라미터에 담아 프론트엔드 에러 페이지로 리디렉션
        String targetUrl = UriComponentsBuilder.fromUriString(FRONTEND_ERROR_URL)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}