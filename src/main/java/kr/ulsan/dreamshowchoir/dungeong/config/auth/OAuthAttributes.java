package kr.ulsan.dreamshowchoir.dungeong.config.auth;

import kr.ulsan.dreamshowchoir.dungeong.domain.user.Role;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String profileImageKey;
    private String oauthId;
    private String provider;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String profileImageKey, String oauthId, String provider) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.profileImageKey = profileImageKey;
        this.oauthId = oauthId;
        this.provider = provider;
    }

    // 넘어온 attributes를 보고 Google인지 Kakao인지 판단하여 파싱
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        // Naver
        if ("naver".equals(registrationId)) {
            return ofNaver(userNameAttributeName, attributes);
        }

        // Kakao
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }

        // Facebook
        if ("facebook".equals(registrationId)) {
            return ofFacebook(userNameAttributeName, attributes);
        }

        // 기본값은 Google
        return ofGoogle(userNameAttributeName, attributes);
    }

    // Google JSON 파싱 메소드
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImageKey((String) attributes.get("picture"))
                .oauthId((String) attributes.get("sub"))
                .provider("google")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Facebook JSON 파싱 메소드
    private static OAuthAttributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {

        // Facebook의 'picture'는 중첩 객체(picture.data.url) 안에 있음
        String profileImageUrl = null;
        if (attributes.containsKey("picture")) {
            Map<String, Object> picture = (Map<String, Object>) attributes.get("picture");
            Map<String, Object> data = (Map<String, Object>) picture.get("data");
            profileImageUrl = (String) data.get("url");
        }

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImageKey(profileImageUrl) // 파싱한 이미지 URL
                .oauthId((String) attributes.get("id")) // 페이스북 고유 ID
                .provider("facebook")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // Naver JSON 파싱 메소드
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        // "response" 키로 중첩된 Map을 한 번 더 가져와야 함
        Map<String, Object> response = (Map<String, Object>) attributes.get(userNameAttributeName); // userNameAttributeName == "response"

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .profileImageKey((String) response.get("profile_image"))
                .oauthId((String) response.get("id")) // 네이버 고유 ID
                .provider("naver")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName) // "response"
                .build();
    }

    // 카카오 파싱 메소드
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // "kakao_account" Map
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        String nickname = null;
        String profileImageUrl = null;
        String email = null;

        // 프로필 정보 (선택 동의)
        if (kakaoAccount.containsKey("profile")) {
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            nickname = (String) profile.get("nickname");
            profileImageUrl = (String) profile.get("profile_image_url");
        }

        // 이메일 정보 (선택 동의)
        if (kakaoAccount.containsKey("email")) {
            email = (String) kakaoAccount.get("email");
        }

        return OAuthAttributes.builder()
                .name(nickname)
                .email(email)
                .profileImageKey(profileImageUrl)
                .oauthId(String.valueOf(attributes.get(userNameAttributeName))) // "id"
                .provider("kakao")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }


    // AuthService가 사용할 User 엔티티 생성
    // (이 DTO는 AuthService에 의존하지 않도록 만듦)
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .profileImageKey(profileImageKey)
                .oauthId(oauthId)
                .oauthProvider(provider)
                .role(Role.USER) // 가입 시 기본 권한
                .build();
    }
}