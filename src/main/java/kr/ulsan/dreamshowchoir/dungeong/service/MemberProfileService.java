package kr.ulsan.dreamshowchoir.dungeong.service;

import kr.ulsan.dreamshowchoir.dungeong.domain.user.MemberProfile;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.MemberProfileRepository;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final S3Service s3Service;

    /**
     * 단원 프로필 사진 수정(업로드)
     *
     * @param userId 로그인한 사용자 ID (PK)
     * @param file   업로드할 이미지 파일
     * @return 수정된 프로필 정보 DTO
     */
    public UserResponseDto updateProfileImage(Long userId, MultipartFile file) {

        // 프로필 조회
        MemberProfile profile = memberProfileRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 단원 프로필이 존재하지 않습니다: " + userId));

        // 기존 이미지가 있다면 S3에서 삭제 (선택 사항: 용량 관리를 위해 삭제 추천)
        // (기존에 저장된 값이 URL 형태라면 S3Service가 알아서 Key를 파싱해서 삭제함)
        if (profile.getProfileImageKey() != null) {
            s3Service.deleteFile(profile.getProfileImageKey());
        }

        // 새 이미지 S3 업로드 ("profile" 폴더에 저장)
        String imageUrl = s3Service.uploadFile(file, "profile");

        // 엔티티 업데이트 (URL 저장)
        // (참고: MemberProfile 엔티티에 updateProfileImage 헬퍼 메소드가 필요합니다)
        profile.updateProfileImage(imageUrl);

        // 변경된 정보를 DTO로 반환 (UserResponseDto 재사용)
        return UserResponseDto.builder()
                .user(profile.getUser())
                .profile(profile)
                .build();
    }

    // (TODO: 텍스트 정보 수정 메소드 updateProfileInfo 등 추가)
}