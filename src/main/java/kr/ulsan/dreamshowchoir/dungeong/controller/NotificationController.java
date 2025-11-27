package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.notification.NotificationResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 나의 알림 목록 조회 API
     * (GET /api/notifications)
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotifications(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(notificationService.getMyNotifications(userId));
    }

    /**
     * 알림 읽음 처리 API
     * (PATCH /api/notifications/{notificationId}/read)
     */
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal Long userId
    ) {
        notificationService.readNotification(notificationId, userId);
        return ResponseEntity.ok().build();
    }
}