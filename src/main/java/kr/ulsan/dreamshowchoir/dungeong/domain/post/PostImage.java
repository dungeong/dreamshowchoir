package kr.ulsan.dreamshowchoir.dungeong.domain.post;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "\"PostImage\"")
@EntityListeners(AuditingEntityListener.class)
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID", nullable = false)
    private kr.ulsan.dreamshowchoir.dungeong.domain.post.Post post;

    @Column(name = "IMAGE_KEY", nullable = false, unique = true)
    private String imageKey;

    @Column(name = "IMAGE_NAME", nullable = false)
    private String imageName;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    // 생성자
    @Builder
    public PostImage(kr.ulsan.dreamshowchoir.dungeong.domain.post.Post post, String imageKey, String imageName) {
        this.post = post;
        this.imageKey = imageKey;
        this.imageName = imageName;
    }

    public void setPost(kr.ulsan.dreamshowchoir.dungeong.domain.post.Post post) {
        this.post = post;
    }
}
