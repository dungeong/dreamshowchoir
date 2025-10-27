package kr.ulsan.dreamshowchoir.dungeong.domain.schedule;

import jakarta.persistence.*;
import kr.ulsan.dreamshowchoir.dungeong.domain.common.BaseAuditEntity; // 1. BaseAuditEntity 상속
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "\"PracticeSchedule\"")
@DynamicUpdate
public class PracticeSchedule extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "LOCATION", nullable = false)
    private String location;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;


    public void update(String title, LocalDateTime date, String location, String description) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
    }
}