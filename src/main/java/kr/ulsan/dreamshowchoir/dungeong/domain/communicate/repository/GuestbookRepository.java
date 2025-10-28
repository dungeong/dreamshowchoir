package kr.ulsan.dreamshowchoir.dungeong.domain.communicate.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.Guestbook;
import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.GuestbookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long> {
    // (방명록 목록용) 승인된 방명록만 페이징 조회 (최신순)
    Page<Guestbook> findAllByStatusAndDeletedAtIsNullOrderByCreatedAtDesc(GuestbookStatus status, Pageable pageable);
    // (관리자용) 특정 상태의 방명록 페이징 조회
    Page<Guestbook> findAllByStatusAndDeletedAtIsNull(GuestbookStatus status, Pageable pageable);
}
