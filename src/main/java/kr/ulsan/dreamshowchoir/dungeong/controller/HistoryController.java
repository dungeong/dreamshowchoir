package kr.ulsan.dreamshowchoir.dungeong.controller;

import kr.ulsan.dreamshowchoir.dungeong.dto.HistoryCreateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.HistoryResponseDto;
import kr.ulsan.dreamshowchoir.dungeong.dto.HistoryUpdateRequestDto;
import kr.ulsan.dreamshowchoir.dungeong.service.HistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    /**
     * 연혁 생성 API (ADMIN 전용)
     * (POST /api/history)
     */
    @PostMapping
    public ResponseEntity<HistoryResponseDto> createHistory(
            @Valid @RequestBody HistoryCreateRequestDto requestDto
    ) {
        HistoryResponseDto createdHistory = historyService.createHistory(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHistory);
    }

    /**
     * 연혁 목록 조회 API (전체 공개)
     * (GET /api/history)
     */
    @GetMapping
    public ResponseEntity<List<HistoryResponseDto>> getHistoryList() {
        List<HistoryResponseDto> historyList = historyService.getHistoryList();
        return ResponseEntity.ok(historyList);
    }

    /**
     * U: 연혁 수정 API (ADMIN 전용)
     * (PATCH /api/history/{historyId})
     */
    @PatchMapping("/{historyId}")
    public ResponseEntity<HistoryResponseDto> updateHistory(
            @PathVariable Long historyId,
            @Valid @RequestBody HistoryUpdateRequestDto requestDto
    ) {
        HistoryResponseDto updatedHistory = historyService.updateHistory(historyId, requestDto);
        return ResponseEntity.ok(updatedHistory);
    }

    /**
     * D: 연혁 삭제 API (ADMIN 전용)
     * (DELETE /api/history/{historyId})
     */
    @DeleteMapping("/{historyId}")
    public ResponseEntity<Void> deleteHistory(
            @PathVariable Long historyId
    ) {
        historyService.deleteHistory(historyId);
        return ResponseEntity.noContent().build();
    }
}