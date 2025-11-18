package kr.ulsan.dreamshowchoir.dungeong.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteContentUpdateRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String content; // 내용은 비어있을 수 있음
}