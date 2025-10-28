package kr.ulsan.dreamshowchoir.dungeong.domain.donation.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.donation.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    // 특정 유저의 후원 내역 전체 조회 (마이페이지용)
    List<Donation> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}
