package kr.ulsan.dreamshowchoir.dungeong.domain.gallery.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.gallery.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    // 갤러리 목록 조회 (최신순) - 페이징
    @Query("SELECT g FROM Gallery g WHERE g.deletedAt IS NULL ORDER BY g.createdAt DESC")
    Page<Gallery> findAllByDeletedAtIsNull(Pageable pageable);
    // 갤러리 상세 조회 시 Media 리스트 Fetch Join (N+1 방지)
    @Query("SELECT g FROM Gallery g LEFT JOIN FETCH g.galleryMedia WHERE g.galleryId = :galleryId AND g.deletedAt IS NULL")
    Optional<Gallery> findByIdWithMedia(Long galleryId);
}
