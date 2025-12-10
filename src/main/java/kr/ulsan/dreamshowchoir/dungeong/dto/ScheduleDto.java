package kr.ulsan.dreamshowchoir.dungeong.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ScheduleDto {
    private String id; // Google Event ID
    private String summary; // Title
    private String description;
    private String location;
    private LocalDateTime start; // Use LocalDateTime for internal logic
    private LocalDateTime end;
}
