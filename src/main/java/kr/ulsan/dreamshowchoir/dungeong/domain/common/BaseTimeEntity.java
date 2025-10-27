package kr.ulsan.dreamshowchoir.dungeong.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseTimeEntity extends BaseAuditEntity{

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;

    // 논리 삭제 수행
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
