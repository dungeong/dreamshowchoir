package kr.ulsan.dreamshowchoir.dungeong.dto.user;

import kr.ulsan.dreamshowchoir.dungeong.domain.user.JoinApplication;
import kr.ulsan.dreamshowchoir.dungeong.domain.user.JoinStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JoinApplicationResponseDto {

    private Long joinId;
    private Long userId;
    private String part;
    private String interests;
    private String myDream;
    private String hashTags;
    private JoinStatus status;
    private LocalDateTime createdAt;

    /**
     * Entity를 DTO로 변환하는 생성자
     */
    public JoinApplicationResponseDto(JoinApplication application) {
        this.joinId = application.getJoinId();
        this.userId = application.getUser().getUserId();
        this.part = application.getPart();
        this.interests = application.getInterests();
        this.myDream = application.getMyDream();
        this.hashTags = application.getHashTags();
        this.status = application.getStatus();
        this.createdAt = application.getCreatedAt();
    }
}