package kr.ulsan.dreamshowchoir.dungeong.domain.notification.repository;

import kr.ulsan.dreamshowchoir.dungeong.config.JpaAuditingConfig;
import kr.ulsan.dreamshowchoir.dungeong.domain.notification.Notification;
import kr.ulsan.dreamshowchoir.dungeong.domain.notification.NotificationType;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.Role;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(JpaAuditingConfig.class)
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User savedTestUser;

    @BeforeEach
    void setUp() {
        // User 저장
        User testUser = User.builder()
                .name("알림받는유저")
                .email("noti@example.com")
                .oauthProvider("google")
                .oauthId("google_noti_123")
                .role(Role.USER)
                .build();
        savedTestUser = userRepository.saveAndFlush(testUser);
    }

    @Test
    @DisplayName("새로운 Notification을 저장하고 ID로 조회하면 성공")
    void saveAndFindNotificationTest() {
        // given (준비)
        Notification newNotification = Notification.builder()
                .user(savedTestUser)
                .type(NotificationType.NEW_COMMENT)
                .message("새로운 댓글이 달렸습니다.")
                .build();

        // when (실행)
        Notification savedNotification = notificationRepository.save(newNotification);

        // then (검증)
        Notification foundNotification = notificationRepository.findById(savedNotification.getNotificationId()).orElseThrow();

        assertThat(foundNotification.getNotificationId()).isEqualTo(savedNotification.getNotificationId());
        assertThat(foundNotification.getMessage()).isEqualTo("새로운 댓글이 달렸습니다.");
        assertThat(foundNotification.getType()).isEqualTo(NotificationType.NEW_COMMENT);
        assertThat(foundNotification.isRead()).isFalse();
        assertThat(foundNotification.getUser().getName()).isEqualTo("알림받는유저");
        assertThat(foundNotification.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("특정 유저의 읽지 않은 알림 목록을 최신순으로 조회함")
    void findByUser_UserIdAndIsReadFalseOrderByCreatedAtDescTest() {
        // given (준비)
        notificationRepository.saveAndFlush(Notification.builder()
                .user(savedTestUser)
                .type(NotificationType.NEW_COMMENT)
                .message("읽지 않은 알림 1")
                .build());

        Notification readNotification = notificationRepository.saveAndFlush(Notification.builder()
                .user(savedTestUser)
                .type(NotificationType.JOIN_APPROVED)
                .message("이미 읽은 알림")
                .build());
        readNotification.read(); // (엔티티 편의 메소드 이름 수정: markAsRead -> read)
        notificationRepository.saveAndFlush(readNotification);

        Notification unreadNotification2 = notificationRepository.saveAndFlush(Notification.builder()
                .user(savedTestUser)
                .type(NotificationType.NEW_COMMENT)
                .message("읽지 않은 알림 2")
                .build());

        // when (실행)
        List<Notification> unreadNotifications = notificationRepository.findByUser_UserIdAndIsReadFalseOrderByCreatedAtDesc(savedTestUser.getUserId());

        // then (검증)
        assertThat(unreadNotifications).hasSize(2);
        assertThat(unreadNotifications.get(0).getNotificationId()).isEqualTo(unreadNotification2.getNotificationId());
        assertThat(unreadNotifications.get(0).getMessage()).isEqualTo("읽지 않은 알림 2");
    }

    @Test
    @DisplayName("알림을 '읽음' 상태로 변경")
    void readTest() {
        // given (준비)
        Notification notification = notificationRepository.save(Notification.builder()
                .user(savedTestUser)
                .type(NotificationType.NEW_NOTICE)
                .message("이 알림을 읽을 겁니다.")
                .build());
        assertThat(notification.isRead()).isFalse();

        // when (실행)
        notification.read();
        notificationRepository.saveAndFlush(notification);

        // then (검증)
        Notification foundNotification = notificationRepository.findById(notification.getNotificationId()).orElseThrow();
        assertThat(foundNotification.isRead()).isTrue();
    }
}