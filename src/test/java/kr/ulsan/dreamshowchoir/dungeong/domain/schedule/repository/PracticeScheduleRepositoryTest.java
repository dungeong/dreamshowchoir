package kr.ulsan.dreamshowchoir.dungeong.domain.schedule.repository;

import kr.ulsan.dreamshowchoir.dungeong.config.JpaAuditingConfig;
import kr.ulsan.dreamshowchoir.dungeong.domain.schedule.PracticeSchedule;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // PostgreSQL 사용
@Import(JpaAuditingConfig.class) // Auditing 기능(BaseAuditEntity) 활성화
class PracticeScheduleRepositoryTest {

    @Autowired
    private PracticeScheduleRepository practiceScheduleRepository;

    @Autowired
    private UserRepository userRepository; // PracticeSchedule은 User에 의존

    private User savedTestUser; // 일정 등록자

    @BeforeEach
    void setUp() {
        // 1. User 저장
        User testUser = User.builder()
                .name("일정담당자")
                .email("schedule@example.com")
                .oauthProvider("google")
                .oauthId("google_schedule_123")
                .role(Role.MEMBER)
                .build();
        savedTestUser = userRepository.saveAndFlush(testUser);
    }

    @Test
    @DisplayName("새로운 PracticeSchedule을 저장하고 ID로 조회하면 성공")
    void saveAndFindPracticeScheduleTest() {
        // given (준비)
        LocalDateTime practiceDate = LocalDateTime.of(2025, 11, 10, 19, 0, 0);

        PracticeSchedule newSchedule = PracticeSchedule.builder()
                .user(savedTestUser)
                .title("11월 10일 정기 연습")
                .date(practiceDate)
                .location("울산 남구 연습실 A")
                .description("정기 공연 대비 연습입니다.")
                .build();

        // when (실행)
        PracticeSchedule savedSchedule = practiceScheduleRepository.save(newSchedule);

        // then (검증)
        PracticeSchedule foundSchedule = practiceScheduleRepository.findById(savedSchedule.getScheduleId()).orElseThrow();

        assertThat(foundSchedule.getScheduleId()).isEqualTo(savedSchedule.getScheduleId());
        assertThat(foundSchedule.getTitle()).isEqualTo("11월 10일 정기 연습");
        assertThat(foundSchedule.getDate()).isEqualTo(practiceDate);
        assertThat(foundSchedule.getUser().getName()).isEqualTo("일정담당자");
        assertThat(foundSchedule.getCreatedAt()).isNotNull(); // BaseAuditEntity 검증
    }

    @Test
    @DisplayName("특정 기간(Between) 사이의 연습 일정을 조회함")
    void findAllByDateBetweenTest() {
        // given (준비)
        LocalDateTime novStart = LocalDateTime.of(2025, 11, 1, 0, 0, 0);
        LocalDateTime novEnd = LocalDateTime.of(2025, 11, 30, 23, 59, 59);
        LocalDateTime octPractice = LocalDateTime.of(2025, 10, 28, 19, 0, 0); // 조회 범위 밖
        LocalDateTime novPractice = LocalDateTime.of(2025, 11, 15, 19, 0, 0); // 조회 범위 안

        // 1. 10월 연습 (범위 밖)
        practiceScheduleRepository.save(PracticeSchedule.builder()
                .user(savedTestUser)
                .title("10월 연습")
                .date(octPractice)
                .location("연습실")
                .build());

        // 2. 11월 연습 (범위 안)
        practiceScheduleRepository.save(PracticeSchedule.builder()
                .user(savedTestUser)
                .title("11월 연습")
                .date(novPractice)
                .location("연습실")
                .build());

        // when (실행)
        List<PracticeSchedule> schedules = practiceScheduleRepository.findAllByDateBetween(novStart, novEnd);

        // then (검증)
        assertThat(schedules).hasSize(1); // 11월 연습 1개만 조회되어야 함
        assertThat(schedules.get(0).getTitle()).isEqualTo("11월 연습");
    }
}