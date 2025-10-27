package kr.ulsan.dreamshowchoir.dungeong.domain.communicate;

import jakarta.persistence.*;
import kr.ulsan.dreamshowchoir.dungeong.domain.common.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "\"Guestbook\"")
@SQLDelete(sql = "UPDATE \"Guestbook\" SET DELETED_AT = CURRENT_TIMESTAMP WHERE GUESTBOOK_ID = ?")
@Where(clause = "DELETED_AT IS NULL")
@DynamicUpdate
public class Guestbook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GUESTBOOK_ID")
    private Long guestbookId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private GuestbookStatus status = GuestbookStatus.APPROVED;

    // 방명록 수정
    public void update(String content) {
        this.content = content;
    }

    // 방명록 승인/숨김
    public void approve() {
        this.status = GuestbookStatus.APPROVED;
    }

    public void hide() {
        this.status = GuestbookStatus.HIDDEN;
    }

}
