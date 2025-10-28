package kr.ulsan.dreamshowchoir.dungeong.domain.sheet.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.sheet.Sheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SheetRepository extends JpaRepository<Sheet, Long> {
    // 악보/자료 목록 조회 (공개 자료만, 최신순)
    Page<Sheet> findByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);
    // 악보/자료 목록 전체 조회 (단원/관리자용, 최신순)
    Page<Sheet> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
