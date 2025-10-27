package kr.ulsan.dreamshowchoir.dungeong.domain.post;

import jakarta.persistence.*;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "\"Comment\"")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE \"Comment\" SET DELETED_AT = CURRENT_TIMESTAMP WHERE COMMENT_ID = ?")
@Where(clause = "DELETED_AT IS NULL")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "CONTENT", nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;

    // 생성자
    @Builder
    public Comment(Post post, User user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
