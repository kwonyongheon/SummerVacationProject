package SummerVacationProject.HCI.web.dto;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GalleryListViewResponse {
    private final Long id;
    private final LocalDateTime createdAt;
    private final String description;
    private final String name;
    private final Member creator;


    public GalleryListViewResponse(Gallery Gallery) {
        this.id = Gallery.getGalleryId();
        this.createdAt = Gallery.getCreatedAt();
        this.description = Gallery.getDescription();
        this.name = Gallery.getName();
        this.creator = Gallery.getCreator();
    }
}
