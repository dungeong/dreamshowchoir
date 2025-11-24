package kr.ulsan.dreamshowchoir.dungeong.dto.sheet;

import kr.ulsan.dreamshowchoir.dungeong.domain.sheet.Sheet;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class SheetResponseDto {
    private final Long sheetId;
    private final String fileName;
    private final String fileKey;
    private final Long fileSize;
    private final String uploaderName;
    private final LocalDateTime createdAt;

    public SheetResponseDto(Sheet sheet) {
        this.sheetId = sheet.getSheetId();
        this.fileName = sheet.getFileName();
        this.fileKey = sheet.getFileKey();
        this.fileSize = sheet.getFileSize();
        this.uploaderName = sheet.getUser().getName();
        this.createdAt = sheet.getCreatedAt();
    }
}