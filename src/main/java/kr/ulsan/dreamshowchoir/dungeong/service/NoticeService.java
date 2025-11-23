package kr.ulsan.dreamshowchoir.dungeong.service;

import kr.ulsan.dreamshowchoir.dungeong.domain.notice.Notice;
import kr.ulsan.dreamshowchoir.dungeong.domain.notice.repository.NoticeRepository;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.Role;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import kr.ulsan.dreamshowchoir.dungeong.dto.common.PageResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.notice.NoticeCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.notice.NoticeListResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.notice.NoticeResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.notice.NoticeUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    /**
     * (공통 메소드) 해당 유저가 ADMIN인지 확인하는 헬퍼 메소드
     */
    private User checkAdminAuthority(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 유저를 찾을 수 없습니다: " + userId));

        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("이 작업을 수행할 권한(ADMIN)이 없습니다.");
        }

        return currentUser;
    }

    /**
     * 새로운 공지사항을 생성 (ADMIN 전용)
     *
     * @param requestDto 공지사항 제목, 내용 DTO
     * @param userId     현재 인증된 사용자의 ID (JWT 토큰에서 추출)
     * @return 생성된 공지사항의 상세 정보 DTO
     */
    public NoticeResponseDto createNotice(NoticeCreateRequestDto requestDto, Long userId) {

        // 작성자(User) 엔티티를 DB에서 조회
        User author = checkAdminAuthority(userId);

        // DTO의 toEntity() 헬퍼 메소드를 사용해 Notice 엔티티를 생성
        Notice newNotice = requestDto.toEntity(author);

        // Repository를 통해 엔티티를 DB에 저장
        Notice savedNotice = noticeRepository.save(newNotice);

        // 저장된 엔티티를 Response DTO로 변환하여 컨트롤러에 반환
        return new NoticeResponseDto(savedNotice);
    }


    /**
     * 공지사항 목록 조회 (MEMBER 이상)
     *
     * @param pageable "page", "size", "sort" 파라미터를 담은 객체
     * @return 페이징 정보와 공지사항 목록 DTO
     */
    @Transactional(readOnly = true)
    public PageResponseDto<NoticeListResponseDto> getNoticeList(Pageable pageable) {

        // Repository에서 Fetch Join이 적용된 쿼리 호출
        Page<Notice> noticePage = noticeRepository.findAllWithUser(pageable);

        // Page<Notice> (엔티티) -> Page<NoticeListResponseDto> (DTO) 변환
        Page<NoticeListResponseDto> dtoPage = noticePage.map(NoticeListResponseDto::new);

        // PageResponseDto(범용 DTO)로 감싸서 반환
        return new PageResponseDto<>(dtoPage);
    }


    /**
     * 공지사항 상세 조회 (MEMBER 이상)
     *
     * @param noticeId 조회할 공지사항의 ID
     * @return 공지사항 상세 정보 DTO
     */
    @Transactional(readOnly = true)
    public NoticeResponseDto getNoticeDetail(Long noticeId) {

        // Repository에서 Fetch Join 쿼리(findByIdWithUser)를 호출
        Notice notice = noticeRepository.findByIdWithUser(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 공지사항을 찾을 수 없습니다: " + noticeId));

        // Notice 엔티티를 NoticeResponseDto(상세 DTO)로 변환하여 반환
        return new NoticeResponseDto(notice);
    }

    /**
     * 공지사항 수정 (ADMIN 전용)
     *
     * @param noticeId   수정할 공지사항의 ID
     * @param requestDto 수정할 제목, 내용 DTO
     * @param userId     현재 인증된 사용자의 ID (권한 검사용)
     * @return 수정된 공지사항의 상세 정보 DTO
     */
    public NoticeResponseDto updateNotice(Long noticeId, NoticeUpdateRequestDto requestDto, Long userId) {

        // (권한 검사) ADMIN인지 확인
        // (SecurityConfig에서 1차로 막지만, 서비스 계층에서도 2차 검증)
        checkAdminAuthority(userId);

        // 공지사항 조회
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 공지사항을 찾을 수 없습니다: " + noticeId));

        // 엔티티 내용 변경
        // (TODO: Notice 엔티티에 update(title, content) 헬퍼 메소드 추가 필요)
        notice.update(requestDto.getTitle(), requestDto.getContent());

        // updatedAt 갱신을 위해 flush() 호출
        noticeRepository.flush();

        // 변경된 엔티티를 DTO로 변환하여 반환
        return new NoticeResponseDto(notice);
    }


    /**
     * 공지사항 삭제(ADMIN 전용)
     *
     * @param noticeId 삭제할 공지사항의 ID
     * @param userId   현재 인증된 사용자의 ID (권한 검사용)
     */
    public void deleteNotice(Long noticeId, Long userId) {

        // (권한 검사) ADMIN인지 확인
        checkAdminAuthority(userId);

        // 공지사항 조회 (삭제할 대상 확인)
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 공지사항을 찾을 수 없습니다: " + noticeId));

        // Repository의 delete() 호출 -> @SQLDelete(논리삭제) 쿼리 실행
        noticeRepository.delete(notice);
    }


}