package kr.ulsan.dreamshowchoir.dungeong.domain.notification;

import jakarta.persistence.*;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "\"Notification\"")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private NotificationType type;

    @Column(name = "MESSAGE", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "IS_READ", nullable = false)
    private boolean isRead;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    // 생성자
    @Builder
    public Notification(User user, NotificationType type, String message) {
        this.user = user;
        this.type = type;
        this.message = message;
        this.isRead = false; // 6. 생성 시 기본값은 '읽지 않음'
    }

    // 읽음 처리
    public void read() {
        this.isRead = true;
    }
}