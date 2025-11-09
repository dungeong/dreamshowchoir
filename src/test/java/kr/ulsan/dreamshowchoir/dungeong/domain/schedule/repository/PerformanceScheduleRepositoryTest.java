package kr.ulsan.dreamshowchoir.dungeong.domain.schedule.repository;

import kr.ulsan.dreamshowchoir.dungeong.config.JpaAuditingConfig;
import kr.ulsan.dreamshowchoir.dungeong.domain.schedule.PerformanceSchedule;
// User 엔티티는 필요하지 않습니다.
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
@Import(JpaAuditingConfig.class) // Auditing 기능(BaseTimeEntity) 활성화
class PerformanceScheduleRepositoryTest {

    @Autowired
    private PerformanceScheduleRepository performanceScheduleRepository;

    // PerformanceSchedule은 User에 의존하지 않음

    @Test
    @DisplayName("새로운 PerformanceSchedule을 저장하고 ID로 조회하면 성공")
    void saveAndFindPerformanceScheduleTest() {
        // given (준비)
        LocalDateTime performanceDate = LocalDateTime.of(2025, 12, 20, 19, 30, 0);

        PerformanceSchedule newSchedule = PerformanceSchedule.builder()
                .title("제 4회 정기연주회")
                .date(performanceDate)
                .location("울산 중구문화의전당")
                .description("전석 초대")
                .ticketLink("http://ticket.example.com")
                .build();

        // when (실행)
        PerformanceSchedule savedSchedule = performanceScheduleRepository.save(newSchedule);

        // then (검증)
        PerformanceSchedule foundSchedule = performanceScheduleRepository.findById(savedSchedule.getPerformanceId()).orElseThrow();

        assertThat(foundSchedule.getPerformanceId()).isEqualTo(savedSchedule.getPerformanceId());
        assertThat(foundSchedule.getTitle()).isEqualTo("제 4회 정기연주회");
        assertThat(foundSchedule.getDate()).isEqualTo(performanceDate);
        assertThat(foundSchedule.getLocation()).isEqualTo("울산 중구문화의전당");
        assertThat(foundSchedule.getTicketLink()).isEqualTo("http://ticket.example.com");
        assertThat(foundSchedule.getCreatedAt()).isNotNull(); // BaseTimeEntity 검증
    }

    @Test
    @DisplayName("특정 기간(Between) 사이의 공연 일정을 (논리 삭제 제외) 조회함")
    void findAllByDateBetweenAndDeletedAtIsNullTest() {
        // given (준비)
        LocalDateTime decStart = LocalDateTime.of(2025, 12, 1, 0, 0, 0);
        LocalDateTime decEnd = LocalDateTime.of(2025, 12, 31, 23, 59, 59);
        LocalDateTime novPerformance = LocalDateTime.of(2025, 11, 20, 19, 0, 0); // (조회 범위 밖)
        LocalDateTime decPerformance = LocalDateTime.of(2025, 12, 20, 19, 0, 0); // (조회 범위 안)

        // 1. 11월 공연 (범위 밖)
        performanceScheduleRepository.save(PerformanceSchedule.builder()
                .title("11월 공연")
                .date(novPerformance)
                .location("공연장")
                .build());

        // 2. 12월 공연 (범위 안)
        performanceScheduleRepository.save(PerformanceSchedule.builder()
                .title("12월 공연")
                .date(decPerformance)
                .location("공연장")
                .build());

        // when (실행)
        List<PerformanceSchedule> schedules = performanceScheduleRepository.findAllByDateBetweenAndDeletedAtIsNull(decStart, decEnd);

        // then (검증)
        assertThat(schedules).hasSize(1); // 12월 공연 1개만 조회되어야 함
        assertThat(schedules.get(0).getTitle()).isEqualTo("12월 공연");
        assertThat(schedules.get(0).getDate()).isEqualTo(decPerformance);
    }

    @Test
    @DisplayName("PerformanceSchedule을 논리 삭제하면 조회되지 않아야 함")
    void softDeleteTest() {
        // given (준비)
        PerformanceSchedule newSchedule = PerformanceSchedule.builder()
                .title("삭제될 공연")
                .date(LocalDateTime.now())
                .location("공연장")
                .build();
        PerformanceSchedule savedSchedule = performanceScheduleRepository.save(newSchedule);
        Long scheduleId = savedSchedule.getPerformanceId();

        // when (실행)
        performanceScheduleRepository.delete(savedSchedule);
        performanceScheduleRepository.flush(); // DB 즉시 반영

        // then (검증)
        // @Where(clause = "\"DELETED_AT\" IS NULL") 때문에 조회되면 안 됨
        assertThat(performanceScheduleRepository.findById(scheduleId)).isEmpty();
    }
}