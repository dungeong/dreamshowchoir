package kr.ulsan.dreamshowchoir.dungeong.domain.post.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.post.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
