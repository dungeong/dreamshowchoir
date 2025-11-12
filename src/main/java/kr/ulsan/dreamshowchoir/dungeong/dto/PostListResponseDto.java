package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.post.Post;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 게시글 '목록' 조회를 위한 DTO (본문 제외)
 */
@Getter
public class PostListResponseDto {

    private Long postId;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;
    // private int commentCount; // (TODO: 나중에 댓글 개수 추가)

    /**
     * Post 엔티티를 PostListResponseDto로 변환
     */
    public PostListResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.authorName = post.getUser().getName(); // N+1 문제 방지를 위해 Fetch Join 필요
        this.createdAt = post.getCreatedAt();
    }
}