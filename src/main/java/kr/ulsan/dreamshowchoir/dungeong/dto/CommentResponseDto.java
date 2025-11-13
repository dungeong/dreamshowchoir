package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.post.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long commentId;
    private String content;
    private String authorName;
    private Long authorId;
    private Long postId;
    private LocalDateTime createdAt;

    // (TODO: updatedAt은 Comment 엔티티에 없지만, 필요시 추가)

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.authorName = comment.getUser().getName();
        this.authorId = comment.getUser().getUserId();
        this.postId = comment.getPost().getPostId();
        this.createdAt = comment.getCreatedAt();
    }
}