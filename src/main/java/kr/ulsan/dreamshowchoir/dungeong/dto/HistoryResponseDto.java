package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.info.History;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HistoryResponseDto {

    private Long historyId;
    private Integer year;
    private Integer month;
    private String content;
    private LocalDateTime createdAt;

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public HistoryResponseDto(History history) {
        this.historyId = history.getHistoryId();
        this.year = history.getYear();
        this.month = history.getMonth();
        this.content = history.getContent();
        this.createdAt = history.getCreatedAt();
    }
}