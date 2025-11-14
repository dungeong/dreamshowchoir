package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.notice.Notice;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponseDto {

    private Long noticeId;
    private String title;
    private String content;
    private String authorName;
    private Long authorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // (TODO: 나중에 NoticeImage 리스트 추가)

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public NoticeResponseDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.content = notice.getContent();
        this.authorName = notice.getUser().getName();
        this.authorId = notice.getUser().getUserId();
        this.createdAt = notice.getCreatedAt();
        this.updatedAt = notice.getUpdatedAt();
    }
}