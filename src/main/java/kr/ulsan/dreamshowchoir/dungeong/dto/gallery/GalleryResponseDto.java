package kr.ulsan.dreamshowchoir.dungeong.dto.gallery;

import kr.ulsan.dreamshowchoir.dungeong.domain.gallery.Gallery;
import kr.ulsan.dreamshowchoir.dungeong.domain.gallery.GalleryMedia;
import kr.ulsan.dreamshowchoir.dungeong.dto.user.UserResponseDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GalleryResponseDto {

    private final Long galleryId;
    private final String type;
    private final String title;
    private final String description;
    private final UserResponseDto author;
    private final LocalDateTime createdAt;
    private final List<GalleryMediaResponseDto> mediaList;

    // 생성자
    public GalleryResponseDto(Gallery gallery, List<GalleryMedia> mediaListEntity) {
        this.galleryId = gallery.getGalleryId();
        this.type = gallery.getType();
        this.title = gallery.getTitle();
        this.description = gallery.getDescription();

        // UserResponseDto 생성 (User와 Profile 모두 전달)
        this.author = new UserResponseDto(gallery.getUser(), gallery.getUser().getMemberProfile());

        this.createdAt = gallery.getCreatedAt();

        // 엔티티 리스트를 DTO 리스트로 변환
        this.mediaList = mediaListEntity.stream()
                .map(media -> new GalleryMediaResponseDto(media))
                .collect(Collectors.toList());
    }
}