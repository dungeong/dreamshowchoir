package kr.ulsan.dreamshowchoir.dungeong.domain.user.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.user.JoinApplication;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.JoinStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JoinApplicationRepository extends JpaRepository<JoinApplication, Long> {
    // 특정 유저의 신청 이력 조회
    Optional<JoinApplication> findByUser_UserId(Long userId);
    // 특정 상태(예 : PENDING)의 신청 목록 조회 (관리자용)
    List<JoinApplication> findByStatus(JoinStatus status);
}
