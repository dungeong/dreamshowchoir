package kr.ulsan.dreamshowchoir.dungeong.dto.inquiry;

import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.Inquiry;
import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.InquiryStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InquiryResponseDto {

    private Long inquiryId;
    private String name;
    private String email;
    private String content;
    private InquiryStatus status;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public InquiryResponseDto(Inquiry inquiry) {
        this.inquiryId = inquiry.getInquiryId();
        this.name = inquiry.getName();
        this.email = inquiry.getEmail();
        this.content = inquiry.getContent();
        this.status = inquiry.getStatus();
        this.answer = inquiry.getAnswer();
        this.createdAt = inquiry.getCreatedAt();
        this.answeredAt = inquiry.getAnsweredAt();
    }
}