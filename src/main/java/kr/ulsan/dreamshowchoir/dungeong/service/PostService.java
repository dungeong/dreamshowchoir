package kr.ulsan.dreamshowchoir.dungeong.service;

import kr.ulsan.dreamshowchoir.dungeong.domain.post.Post;
import kr.ulsan.dreamshowchoir.dungeong.domain.post.repository.PostRepository;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.Role;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.UserRepository;
import kr.ulsan.dreamshowchoir.dungeong.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 필드 생성자 자동 주입
@Transactional // 클래스 레벨에 트랜잭션 선언 (모든 public 메소드에 적용)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 새로운 게시글을 생성한다.
     *
     * @param requestDto 게시글 제목, 내용 DTO
     * @param userId     현재 인증된 사용자의 ID (JWT 토큰에서 추출)
     * @return 생성된 게시글의 상세 정보 DTO
     */
    public PostResponseDto createPost(PostCreateRequestDto requestDto, Long userId) {

        // 작성자(User) 엔티티를 DB에서 조회
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없습니다: " + userId));

        // DTO의 toEntity() 헬퍼 메소드를 사용해 Post 엔티티를 생성
        Post newPost = requestDto.toEntity(author);

        // Repository를 통해 엔티티를 DB에 저장
        Post savedPost = postRepository.save(newPost);

        // 저장된 엔티티를 Response DTO로 변환하여 컨트롤러에 반환
        return new PostResponseDto(savedPost);
    }

    /**
     * 게시글 목록을 페이징하여 조회
     *
     * @param pageable "page", "size", "sort" 파라미터를 담은 객체
     * @return 페이징 정보와 게시글 목록 DTO
     */
    @Transactional(readOnly = true) // 조회(SELECT)만 하므로 readOnly = true (성능 최적화)
    public PageResponseDto<PostListResponseDto> getPostList(Pageable pageable) {

        // Repository에서 Fetch Join이 적용된 쿼리 호출
        // (N+1 문제 방지: Post 조회 시 User(authorName)도 함께 조회)
        Page<Post> postPage = postRepository.findAllWithUser(pageable);

        // Page<Post> (엔티티)를 Page<PostListResponseDto> (DTO)로 변환
        // .map()은 Page 객체가 제공하는 변환 기능
        Page<PostListResponseDto> dtoPage = postPage.map(PostListResponseDto::new);

        // Page<DTO>를 우리가 만든 PageResponseDto(범용 DTO)로 감싸서 반환
        return new PageResponseDto<>(dtoPage);
    }

    /**
     * 게시글 1건 상세 조회
     *
     * @param postId 조회할 게시글의 ID
     * @return 게시글 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public PostResponseDto getPostDetail(Long postId) {

        // Repository에서 Fetch Join 쿼리(findByIdWithUser)를 호출
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + postId));

        // Post 엔티티를 PostResponseDto(상세 DTO)로 변환하여 반환
        return new PostResponseDto(post);
    }

    /**
     * 게시글 1건을 수정합니다.
     *
     * @param postId 수정할 게시글의 ID
     * @param requestDto 수정할 제목, 내용 DTO
     * @param userId     현재 인증된 사용자의 ID (JWT 토큰에서 추출)
     * @return 수정된 게시글의 상세 정보 DTO
     */
    public PostResponseDto updatePost(Long postId, PostUpdateRequestDto requestDto, Long userId) {

        // DB에서 게시글 조회 (User 정보 포함)
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + postId));

        // 권한 검사 - 게시글 작성자의 ID와 현재 로그인한 사용자의 ID가 동일한지 확인
        if (!post.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("이 게시글을 수정할 권한이 없습니다.");
        }

        // 엔티티의 update 헬퍼 메소드를 호출하여 내용을 변경
        post.update(requestDto.getTitle(), requestDto.getContent());

        // DB에 변경 사항을 즉시 강제 실행
        postRepository.flush();

        return new PostResponseDto(post);
    }

    /**
     * 게시글 1건을 (논리) 삭제
     *
     * @param postId  삭제할 게시글의 ID
     * @param userId  현재 인증된 사용자의 ID (JWT 토큰에서 추출)
     */
    public void deletePost(Long postId, Long userId) {

        // 게시글을 DB에서 조회 (작성자 정보 포함)
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 게시글을 찾을 수 없습니다: " + postId));

        // 권한 검사 (현재 요청한 사용자가 ADMIN인지 확인하기 위해, 현재 사용자 정보도 조회)
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없습니다: " + userId));

        // 작성자 본인이거나 관리자(ADMIN)가 아니면, 권한 없음 예외 발생
        boolean isOwner = post.getUser().getUserId().equals(userId);
        boolean isAdmin = currentUser.getRole().equals(Role.ADMIN);

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("이 게시글을 삭제할 권한이 없습니다.");
        }

        // Repository의 delete() 호출 -> @SQLDelete(논리삭제) 쿼리 실행
        postRepository.delete(post);
    }
}