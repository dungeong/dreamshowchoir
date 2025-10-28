package kr.ulsan.dreamshowchoir.dungeong.domain.material.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.material.ActivityMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityMaterialRepository extends JpaRepository<ActivityMaterial, Long> {
    // 활동 자료 목록 조회 (페이징, 최신순)
    Page<ActivityMaterial> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
