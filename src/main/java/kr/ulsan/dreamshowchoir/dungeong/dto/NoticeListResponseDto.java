package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.notice.Notice;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 공지사항 '목록' 조회를 위한 DTO (본문 제외)
 */
@Getter
public class NoticeListResponseDto {

    private Long noticeId;
    private String title;
    private String authorName;
    private LocalDateTime createdAt;

    /**
     * Notice 엔티티를 NoticeListResponseDto로 변환
     */
    public NoticeListResponseDto(Notice notice) {
        this.noticeId = notice.getNoticeId();
        this.title = notice.getTitle();
        this.authorName = notice.getUser().getName();
        this.createdAt = notice.getCreatedAt();
    }
}