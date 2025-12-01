package kr.ulsan.dreamshowchoir.dungeong.domain.gallery.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.gallery.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    // 목록 전체 조회 (Type 필터 없음)
    // @EntityGraph: "user" 필드를 같이 가져와라 (LEFT JOIN FETCH와 동일 효과)
    @EntityGraph(attributePaths = {"user"})
    Page<Gallery> findAll(Pageable pageable);

    // 목록 타입별 조회 (Type 필터 있음) - User Fetch Join
    // Enum 파라미터 바인딩 문제를 피하기 위해 String으로 받아서 비교
    @Query("SELECT g FROM Gallery g JOIN FETCH g.user WHERE CAST(g.type AS string) = :type")
    Page<Gallery> findByType(@Param("type") String type, Pageable pageable);

    // 상세 조회 (작성자 정보 함께 로딩)
    @Query("SELECT g FROM Gallery g JOIN FETCH g.user LEFT JOIN FETCH g.galleryMedia WHERE g.galleryId = :galleryId")
    Optional<Gallery> findByIdWithUserAndMedia(@Param("galleryId") Long galleryId);
}
