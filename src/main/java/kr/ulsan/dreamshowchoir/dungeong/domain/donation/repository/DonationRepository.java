package kr.ulsan.dreamshowchoir.dungeong.domain.donation.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.donation.Donation;
import kr.ulsan.dreamshowchoir.dungeong.domain.donation.DonationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    // 특정 유저의 후원 내역 전체 조회 (마이페이지용)
    List<Donation> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    // 특정 상태의 후원 목록을 페이징하여 조회 (관리자용)
    Page<Donation> findByStatus(DonationStatus status, Pageable pageable);

    // 완료된 후원 목록 조회 (금액 내림차순 -> 최신순)
    @Query("SELECT d FROM Donation d JOIN FETCH d.user " +
            "WHERE d.status = 'COMPLETED' " +
            "ORDER BY d.amount DESC, d.createdAt DESC")
    List<Donation> findAllCompletedDonations();
}
