package kr.ulsan.dreamshowchoir.dungeong.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kr.ulsan.dreamshowchoir.dungeong.domain.content.SiteContent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteContentCreateRequestDto {

    @NotBlank(message = "콘텐츠 키(Key)를 입력해주세요.")
    @Size(max = 50, message = "콘텐츠 키는 50자 이내여야 합니다.")
    private String contentKey; // "RECRUIT_GUIDE", "DONATION_GUIDE" 등

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String content; // 내용은 비어있을 수 있음

    /**
     * DTO를 SiteContent 엔티티로 변환
     */
    public SiteContent toEntity() {
        return SiteContent.builder()
                .contentKey(this.contentKey.toUpperCase()) // 키는 대문자로 통일
                .title(this.title)
                .content(this.content)
                .build();
    }
}