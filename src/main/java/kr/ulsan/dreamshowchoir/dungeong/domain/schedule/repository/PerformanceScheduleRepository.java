package kr.ulsan.dreamshowchoir.dungeong.domain.schedule.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.schedule.PerformanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {
    // 특정 기간 사이의 공연 일정 조회 (캘린더용, 논리 삭제 제외)
    List<PerformanceSchedule> findAllByDateBetweenAndDeletedAtIsNull(LocalDateTime start, LocalDateTime end);
    // 다가오는 공연 일정 목록 조회 (논리 삭제 제외)
    List<PerformanceSchedule> findAllByDateAfterAndDeletedAtIsNullOrderByDateAsc(LocalDateTime now);
}
