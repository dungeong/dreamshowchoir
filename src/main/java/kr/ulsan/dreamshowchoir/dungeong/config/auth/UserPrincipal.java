package kr.ulsan.dreamshowchoir.dungeong.config.auth;

import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    // 일반 로그인 생성자 (지금은 안 쓰지만 UserDetails를 위해)
    public UserPrincipal(User user) {
        this.user = user;
    }

    // OAuth2 로그인 생성자
    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 컨트롤러에서 @AuthenticationPrincipal로 주입될 때 User 객체가 아닌, 이 User의 'ID'를 직접 주입하도록 편의 메소드를 만듦
    public Long getUserId() {
        return user.getUserId();
    }

    // ------------- UserDetails 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User의 Role을 Spring Security가 인식하는 권한으로 변환
        return Collections.singleton(
                new SimpleGrantedAuthority(user.getRole().getKey())
        );
    }

    @Override
    public String getPassword() {
        // OAuth 로그인이라 비밀번호는 사용하지 않음
        return null;
    }

    @Override
    public String getUsername() {
        // (PK나 이메일 등 고유 식별자)
        return user.getEmail();
    }

    // (계정 만료/잠금 등은 지금은 사용하지 않으므로 모두 true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getDeletedAt() == null; // (논리 삭제되지 않은 사용자만 활성화)
    }

    // ---------------- OAuth2User 구현
    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        // OAuth2 제공자가 반환하는 사용자의 고유 식별자 (ID)
        return user.getOauthId();
    }
}