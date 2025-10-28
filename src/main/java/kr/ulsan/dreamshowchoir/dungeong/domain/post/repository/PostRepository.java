package kr.ulsan.dreamshowchoir.dungeong.domain.post.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 게시판 목록 조회 (최신순) - User 정보 함께 Fetch Join (N+1 방지)
    @Query("SELECT p FROM Post p JOIN FETCH p.user WHERE p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    Page<Post> findAllWithUser(Pageable pageable);
}
