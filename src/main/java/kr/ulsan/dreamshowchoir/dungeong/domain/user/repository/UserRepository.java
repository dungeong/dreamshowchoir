package kr.ulsan.dreamshowchoir.dungeong.domain.user.repository;


import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 조회 (논리 삭제된 유저 제외)
    Optional<User> findByEmail(String email);
    // OAuth 정보로 조회 (논리 삭제된 유저 제외)
    Optional<User> findByOauthProviderAndOauthId(String oauthProvider, String oauthId);
}