package kr.ulsan.dreamshowchoir.dungeong.dto.donation;

import kr.ulsan.dreamshowchoir.dungeong.domain.donation.Donation;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DonorResponseDto {
    private final String donorName; // 후원자 이름
    private final Long amount;      // 후원 금액
    private final LocalDateTime date; // 후원 날짜

    public DonorResponseDto(Donation donation) {
        // Donation 엔티티에 별도 donorName 필드가 있다면 그걸 쓰고, 없으면 User의 이름을 씀
        this.donorName = donation.getUser().getName();
        this.amount = donation.getAmount();
        this.date = donation.getCreatedAt();
    }
}