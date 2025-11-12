package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    private Long postId;
    private String title;
    private String content;
    private String authorName; // User 엔티티 전체 대신, 작성자 이름만
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // (TODO: PostImage 리스트 추가)
    // private List<PostImageDto> images;

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorName = post.getUser().getName(); // User 객체에서 이름만 추출
        this.authorId = post.getUser().getUserId();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}