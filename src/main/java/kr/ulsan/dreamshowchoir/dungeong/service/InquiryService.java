package kr.ulsan.dreamshowchoir.dungeong.service;

import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.Inquiry;
import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.repository.InquiryRepository;
import kr.ulsan.dreamshowchoir.dungeong.dto.InquiryCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.InquiryReplyRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.InquiryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.PageResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final RecaptchaService recaptchaService;

    /**
     * 문의 생성 (비로그인 사용자)
     */
    public InquiryResponseDto createInquiry(InquiryCreateRequestDto requestDto) {

        // reCAPTCHA 토큰 검증
        boolean isValidRecaptcha = recaptchaService.validateToken(requestDto.getRecaptchaToken());
        if (!isValidRecaptcha) {
            // 스팸(봇)으로 판단되면 403 Forbidden (또는 400 Bad Request)
            throw new AccessDeniedException("유효하지 않은 reCAPTCHA 토큰입니다. (스팸으로 의심됨)");
        }

        // DTO를 Entity로 변환 (Status는 PENDING으로 자동 설정됨)
        Inquiry newInquiry = requestDto.toEntity();

        // DB에 저장
        Inquiry savedInquiry = inquiryRepository.save(newInquiry);

        // DTO로 변환하여 반환
        return new InquiryResponseDto(savedInquiry);
    }

    /**
     * (관리자용) 특정 상태의 문의 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponseDto<InquiryResponseDto> getInquiryListByStatus(String statusString, Pageable pageable) {

        // String을 InquiryStatus Enum으로 변환
        // (StatusUpdateRequestDto와 동일한 로직)
        kr.ulsan.dreamshowchoir.dungeong.domain.communicate.InquiryStatus status;
        try {
            status = kr.ulsan.dreamshowchoir.dungeong.domain.communicate.InquiryStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("요청 상태(PENDING/ANSWERED)가 올바르지 않습니다.");
        }

        // Repository에서 상태별 페이징 조회
        Page<Inquiry> inquiryPage = inquiryRepository.findAllByStatusOrderByCreatedAtDesc(status, pageable);

        // Page<Entity> -> Page<DTO> 변환
        Page<InquiryResponseDto> dtoPage = inquiryPage.map(InquiryResponseDto::new);

        // PageResponseDto(범용 DTO)로 감싸서 반환
        return new PageResponseDto<>(dtoPage);
    }


    /**
     * (관리자용) 답변 추가
     */
    public InquiryResponseDto replyToInquiry(Long inquiryId, InquiryReplyRequestDto requestDto) {

        // 문의 조회
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 문의를 찾을 수 없습니다: " + inquiryId));

        // 이미 답변했는지 확인 (중복 답변 방지)
        if (inquiry.getStatus() == kr.ulsan.dreamshowchoir.dungeong.domain.communicate.InquiryStatus.ANSWERED) {
            throw new IllegalStateException("이미 답변이 완료된 문의입니다.");
        }

        // 엔티티 헬퍼 메소드로 답변 추가 (Status -> ANSWERED, answeredAt 갱신)
        inquiry.addAnswer(requestDto.getAnswer());

        // (TODO: 작성자에게 이메일 알림 전송 로직 추가)

        return new InquiryResponseDto(inquiry);
    }
}