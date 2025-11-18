package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.content.SiteContent;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SiteContentResponseDto {

    private String contentKey;
    private String title;
    private String content;
    private LocalDateTime updatedAt;

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public SiteContentResponseDto(SiteContent siteContent) {
        this.contentKey = siteContent.getContentKey();
        this.title = siteContent.getTitle();
        this.content = siteContent.getContent();
        this.updatedAt = siteContent.getUpdatedAt();
    }
}