package kr.ulsan.dreamshowchoir.dungeong.domain.schedule;

import jakarta.persistence.*;
import kr.ulsan.dreamshowchoir.dungeong.domain.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "\"PerformanceSchedule\"")
@SQLDelete(sql = "UPDATE \"PerformanceSchedule\" SET \"DELETED_AT\" = CURRENT_TIMESTAMP WHERE \"PERFORMANCE_ID\" = ?")
@Where(clause = "\"DELETED_AT\" IS NULL")
@DynamicUpdate
public class PerformanceSchedule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERFORMANCE_ID")
    private Long performanceId;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DATE", nullable = false)
    private LocalDateTime date;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "DESCRIPTION", columnDefinition = "TEXT")
    private String description;

    @Column(name = "TICKET_LINK")
    private String ticketLink;

    // 업데이트
    public void update(String title, LocalDateTime date, String location, String description, String ticketLink) {
        this.title = title;
        this.date = date;
        this.location = location;
        this.description = description;
        this.ticketLink = ticketLink;
    }
}
