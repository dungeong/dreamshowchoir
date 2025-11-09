package kr.ulsan.dreamshowchoir.dungeong.domain.sheet.repository;

import kr.ulsan.dreamshowchoir.dungeong.config.JpaAuditingConfig;
import kr.ulsan.dreamshowchoir.dungeong.domain.sheet.Sheet;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.Role;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.User;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // PostgreSQL 사용
@Import(JpaAuditingConfig.class) // Auditing 기능(BaseTimeEntity) 활성화
class SheetRepositoryTest {

    @Autowired
    private SheetRepository sheetRepository;

    @Autowired
    private UserRepository userRepository; // Sheet는 User에 의존

    private User savedTestUser;

    @BeforeEach
    void setUp() {
        // User 저장
        User testUser = User.builder()
                .name("자료담당자")
                .email("sheet@example.com")
                .oauthProvider("google")
                .oauthId("google_sheet_123")
                .role(Role.MEMBER)
                .build();
        savedTestUser = userRepository.saveAndFlush(testUser);
    }

    @Test
    @DisplayName("새로운 Sheet를 저장하고 ID로 조회하면 성공")
    void saveAndFindSheetTest() {
        // given (준비)
        Sheet newSheet = Sheet.builder()
                .user(savedTestUser)
                .fileKey("s3-sheet-key-123.pdf")
                .fileName("꿈꾸지않으면.pdf")
                .fileSize(2048L) // V2에서 추가한 fileSize
                .isPublic(true) // 공개 자료로 설정
                .build();

        // when (실행)
        Sheet savedSheet = sheetRepository.save(newSheet);

        // then (검증)
        Sheet foundSheet = sheetRepository.findById(savedSheet.getSheetId()).orElseThrow();

        assertThat(foundSheet.getSheetId()).isEqualTo(savedSheet.getSheetId());
        assertThat(foundSheet.getFileName()).isEqualTo("꿈꾸지않으면.pdf");
        assertThat(foundSheet.getFileSize()).isEqualTo(2048L);
        assertThat(foundSheet.isPublic()).isTrue();
        assertThat(foundSheet.getUser().getName()).isEqualTo("자료담당자");
        assertThat(foundSheet.getCreatedAt()).isNotNull(); // BaseTimeEntity 검증
    }

    @Test
    @DisplayName("공개된(isPublic=true) 자료만 페이징 조회함")
    void findByIsPublicTrueTest() {
        // given (준비)
        // 1. 공개 자료
        sheetRepository.saveAndFlush(Sheet.builder()
                .user(savedTestUser)
                .fileKey("key1.pdf")
                .fileName("공개악보1.pdf")
                .isPublic(true)
                .build());

        // 2. 비공개 자료
        sheetRepository.saveAndFlush(Sheet.builder()
                .user(savedTestUser)
                .fileKey("key2.mp3")
                .fileName("비공개음원.mp3")
                .isPublic(false) // 비공개
                .build());

        // 3. 공개 자료 2 (최신)
        Sheet publicSheet2 = sheetRepository.saveAndFlush(Sheet.builder()
                .user(savedTestUser)
                .fileKey("key3.pdf")
                .fileName("공개악보2.pdf")
                .isPublic(true)
                .build());

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when (실행)
        Page<Sheet> publicSheets = sheetRepository.findByIsPublicTrueOrderByCreatedAtDesc(pageRequest);

        // then (검증)
        assertThat(publicSheets.getTotalElements()).isEqualTo(2); // 공개 자료 2개만 조회
        assertThat(publicSheets.getContent()).hasSize(2);
        // 최신순 정렬 검증 (공개악보2가 먼저)
        assertThat(publicSheets.getContent().get(0).getSheetId()).isEqualTo(publicSheet2.getSheetId());
        assertThat(publicSheets.getContent().get(0).getFileName()).isEqualTo("공개악보2.pdf");
    }

    @Test
    @DisplayName("Sheet를 논리 삭제하면 조회되지 않아야 함")
    void softDeleteTest() {
        // given (준비)
        Sheet newSheet = Sheet.builder()
                .user(savedTestUser)
                .fileKey("key_delete.pdf")
                .fileName("삭제될악보.pdf")
                .isPublic(true)
                .build();
        Sheet savedSheet = sheetRepository.save(newSheet);
        Long sheetId = savedSheet.getSheetId();

        // when (실행)
        sheetRepository.delete(savedSheet);
        sheetRepository.flush(); // DB 즉시 반영

        // then (검증)
        // @Where(clause = "\"DELETED_AT\" IS NULL") 때문에 조회되면 안 됨
        assertThat(sheetRepository.findById(sheetId)).isEmpty();
    }
}