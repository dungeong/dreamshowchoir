package kr.ulsan.dreamshowchoir.dungeong.domain.site.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.site.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    // 활성화된 배너 목록 조회
    List<Banner> findAllByIsActiveTrueAndDeletedAtIsNullOrderByOrderIndexAsc();
}
