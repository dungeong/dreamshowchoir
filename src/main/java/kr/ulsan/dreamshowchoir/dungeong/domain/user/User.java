package kr.ulsan.dreamshowchoir.dungeong.domain.user;

import jakarta.persistence.*;
import kr.ulsan.dreamshowchoir.dungeong.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "\"User\"")
@SQLDelete(sql = "UPDATE \"User\" SET \"DELETED_AT\" = CURRENT_TIMESTAMP WHERE \"USER_ID\" = ?")
@Where(clause = "\"DELETED_AT\" IS NULL")
@DynamicUpdate      // 수정 시 변경된 필드만 update
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "OAUTH_PROVIDER", nullable = false)
    private String oauthProvider;

    @Column(name = "OAUTH_ID", nullable = false, unique = true)
    private String oauthId;

    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PROFILE_IMAGE_KEY")
    private String profileImageKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    // 1:1 관계매핑
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberProfile memberProfile;

    // 생성자
    @Builder
    public User(String oauthProvider, String oauthId, String email, String name, String profileImageKey, Role role) {
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.profileImageKey = profileImageKey;
        this.role = role;
    }

    // OAuth2 로그인 시 기존 회원 정보 업데이트
    public User updateOAuthInfo(String name, String profileImageKey) {
        this.name = name;
        this.profileImageKey = profileImageKey;
        return this;
    }

    // 회원 등급 '단원'으로 승급
    public void approveAsMember() {
        this.role = Role.MEMBER;
    }

    // 양방향 관계 편의 메소드
    public void setMemberProfile(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
    }


}
