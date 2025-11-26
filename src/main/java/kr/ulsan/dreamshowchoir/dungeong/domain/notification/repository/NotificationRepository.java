package kr.ulsan.dreamshowchoir.dungeong.domain.notification.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 특정 유저의 (읽지 않은) 알림 목록 조회 (최신순)
    List<Notification> findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

    // 특정 유저의 모든 알림 조회
    List<Notification> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}
