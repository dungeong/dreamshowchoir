package kr.ulsan.dreamshowchoir.dungeong.domain.notice.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    // 공지사항 목록 조회 (최신순) - User 정보 함께 Fetch Join
    @Query("SELECT n FROM Notice n JOIN FETCH n.user WHERE n.deletedAt IS NULL ORDER BY n.createdAt DESC")
    Page<Notice> findAllWithUser(Pageable pageable);
}
