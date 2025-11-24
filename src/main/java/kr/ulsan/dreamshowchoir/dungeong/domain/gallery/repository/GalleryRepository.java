package kr.ulsan.dreamshowchoir.dungeong.domain.gallery.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.gallery.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    // 목록 조회 (작성자 정보 함께 로딩 - N+1 문제 방지, 중복 데이터 방지, 미디어가 없는 갤러리도 조회)
    @Query(value = "SELECT DISTINCT g FROM Gallery g " +
            "JOIN FETCH g.user " +
            "LEFT JOIN FETCH g.galleryMedia",
            countQuery = "SELECT COUNT(g) FROM Gallery g")
    Page<Gallery> findAllWithUser(Pageable pageable);

    // 상세 조회 (작성자 정보 함께 로딩)
    @Query("SELECT g FROM Gallery g JOIN FETCH g.user WHERE g.galleryId = :galleryId")
    Optional<Gallery> findByIdWithUser(@Param("galleryId") Long galleryId);
}
