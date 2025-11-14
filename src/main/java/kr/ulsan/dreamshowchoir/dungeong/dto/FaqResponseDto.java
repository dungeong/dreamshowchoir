package kr.ulsan.dreamshowchoir.dungeong.dto;

import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.Faq;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FaqResponseDto {

    private Long faqId;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public FaqResponseDto(Faq faq) {
        this.faqId = faq.getFaqId();
        this.question = faq.getQuestion();
        this.answer = faq.getAnswer();
        this.createdAt = faq.getCreatedAt();
        this.updatedAt = faq.getUpdatedAt();
    }
}