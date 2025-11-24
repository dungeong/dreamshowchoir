package kr.ulsan.dreamshowchoir.dungeong.dto.donation;

import kr.ulsan.dreamshowchoir.dungeong.domain.donation.Donation;
import kr.ulsan.dreamshowchoir.dungeong.domain.donation.DonationStatus;
import kr.ulsan.dreamshowchoir.dungeong.domain.donation.DonationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DonationResponseDto {

    private final Long donationId;
    private final Long userId;
    private final Long amount;
    private final DonationType type;
    private final DonationStatus status;
    private final LocalDateTime createdAt;

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public DonationResponseDto(Donation donation) {
        this.donationId = donation.getDonationId();
        this.userId = donation.getUser().getUserId();
        this.amount = donation.getAmount();
        this.type = donation.getType();
        this.status = donation.getStatus();
        this.createdAt = donation.getCreatedAt();
    }
}