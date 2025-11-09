package kr.ulsan.dreamshowchoir.dungeong.domain.communicate.repository;

import kr.ulsan.dreamshowchoir.dungeong.config.JpaAuditingConfig;
import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.Guestbook;
import kr.ulsan.dreamshowchoir.dungeong.domain.communicate.GuestbookStatus; // Enum import
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
class GuestbookRepositoryTest {

    @Autowired
    private GuestbookRepository guestbookRepository;


    @Test
    @DisplayName("새로운 Guestbook을 저장하고 ID로 조회하면 성공 (기본 상태 APPROVED)")
    void saveAndFindGuestbookTest() {
        // given (준비)
        Guestbook newGuestbook = Guestbook.builder()
                .name("방문자1")
                .content("홈페이지 멋지네요!")
                .password("1234") // (실제로는 해시 필요)
                .ipAddress("127.0.0.1")
                .build();

        // when (실행)
        Guestbook savedGuestbook = guestbookRepository.save(newGuestbook);

        // then (검증)
        Guestbook foundGuestbook = guestbookRepository.findById(savedGuestbook.getGuestbookId()).orElseThrow();

        assertThat(foundGuestbook.getGuestbookId()).isEqualTo(savedGuestbook.getGuestbookId());
        assertThat(foundGuestbook.getName()).isEqualTo("방문자1");
        assertThat(foundGuestbook.getContent()).isEqualTo("홈페이지 멋지네요!");

        // (V3 마이그레이션 검증) 기본 상태가 'APPROVED'인지 확인
        assertThat(foundGuestbook.getStatus()).isEqualTo(GuestbookStatus.APPROVED);

        assertThat(foundGuestbook.getCreatedAt()).isNotNull(); // BaseTimeEntity 검증
    }

    @Test
    @DisplayName("승인된(APPROVED) 방명록만 페이징 조회함")
    void findAllByStatusAndDeletedAtIsNullTest() {
        // given (준비)
        // 1. 승인된 방명록 1
        guestbookRepository.saveAndFlush(Guestbook.builder()
                .name("방문자1")
                .content("내용1 (승인)")
                .build()); // 기본값 APPROVED

        // 2. 숨김 처리된 방명록
        Guestbook hiddenGuestbook = guestbookRepository.saveAndFlush(Guestbook.builder()
                .name("방문자2")
                .content("내용2 (숨김)")
                .build());
        hiddenGuestbook.hide(); // 상태 변경
        guestbookRepository.saveAndFlush(hiddenGuestbook);

        // 3. 승인된 방명록 2 (최신)
        Guestbook approvedGuestbook2 = guestbookRepository.saveAndFlush(Guestbook.builder()
                .name("방문자3")
                .content("내용3 (승인)")
                .build()); // 기본값 APPROVED

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        // when (실행)
        Page<Guestbook> approvedGuestbooks = guestbookRepository.findAllByStatusAndDeletedAtIsNullOrderByCreatedAtDesc(GuestbookStatus.APPROVED, pageRequest);

        // then (검증)
        assertThat(approvedGuestbooks.getTotalElements()).isEqualTo(2); // 승인된 2개만 조회
        assertThat(approvedGuestbooks.getContent()).hasSize(2);
        // 최신순 정렬 검증 (방문자3이 먼저)
        assertThat(approvedGuestbooks.getContent().get(0).getGuestbookId()).isEqualTo(approvedGuestbook2.getGuestbookId());
        assertThat(approvedGuestbooks.getContent().get(0).getName()).isEqualTo("방문자3");
    }

    @Test
    @DisplayName("Guestbook을 논리 삭제하면 조회되지 않아야 함")
    void softDeleteTest() {
        // given (준비)
        Guestbook newGuestbook = Guestbook.builder()
                .name("삭제될방문자")
                .content("삭제될 글")
                .build();
        Guestbook savedGuestbook = guestbookRepository.save(newGuestbook);
        Long guestbookId = savedGuestbook.getGuestbookId();

        // when (실행)
        guestbookRepository.delete(savedGuestbook);
        guestbookRepository.flush(); // DB 즉시 반영

        // then (검증)
        // @Where(clause = "\"DELETED_AT\" IS NULL") 때문에 조회되면 안 됨
        assertThat(guestbookRepository.findById(guestbookId)).isEmpty();
    }
}