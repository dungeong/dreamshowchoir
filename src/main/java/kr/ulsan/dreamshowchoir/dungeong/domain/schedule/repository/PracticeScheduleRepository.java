package kr.ulsan.dreamshowchoir.dungeong.domain.schedule.repository;

import kr.ulsan.dreamshowchoir.dungeong.domain.schedule.PracticeSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PracticeScheduleRepository extends JpaRepository<PracticeSchedule, Long> {
    // 특정 기간 사이의 연습 일정 조회 (캘린더용)
    List<PracticeSchedule> findAllByDateBetween(LocalDateTime start, LocalDateTime end);
}
