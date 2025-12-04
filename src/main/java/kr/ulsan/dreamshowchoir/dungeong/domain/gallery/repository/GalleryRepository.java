package kr.ulsan.dreamshowchoir.dungeong.domain.gallery.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.gallery.Gallery;
import kr.ulsan.dreamshowchoir.dungeong.domain.gallery.GalleryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    /**
     * 목록 전체 조회 (Type 필터 없음)
     * @EntityGraph: "user" 필드를 같이 가져와라 (LEFT JOIN FETCH와 동일 효과)
     * 메서드 이름 : findAll -> 전체 조회
     */
    @EntityGraph(attributePaths = {"user"})
    Page<Gallery> findAll(Pageable pageable);

    /**
     * 목록 타입별 조회 (Type 필터 있음) - User Fetch Join
     * Enum 파라미터 바인딩 문제를 피하기 위해 String으로 받아서 비교
     * 메서드 이름 : findByType -> "WHERE type = ?" 쿼리 자동 생성
     * 메서드 이름으로 만들면 Hibernate가 Enum 타입을 알아서 처리함
     */
    @EntityGraph(attributePaths = {"user"})
    Page<Gallery> findByType(GalleryType type, Pageable pageable);

    /**
     * 상세 조회
     */
    @Query("SELECT g FROM Gallery g JOIN FETCH g.user LEFT JOIN FETCH g.galleryMedia WHERE g.galleryId = :galleryId")
    Optional<Gallery> findByIdWithUserAndMedia(@Param("galleryId") Long galleryId);
}
