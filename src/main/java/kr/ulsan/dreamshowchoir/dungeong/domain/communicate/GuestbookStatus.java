package kr.ulsan.dreamshowchoir.dungeong.domain.communicate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GuestbookStatus {
    APPROVED("APPROVED", "승인됨"),
    HIDDEN("HIDDEN", "숨김");

    private final String key;
    private final String title;
}
