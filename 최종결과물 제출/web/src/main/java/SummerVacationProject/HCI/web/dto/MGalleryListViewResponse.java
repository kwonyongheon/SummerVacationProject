package SummerVacationProject.HCI.web.dto;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MGalleryListViewResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final String description;
    private final String name;
    private final Member creator;


    public MGalleryListViewResponse(MinorGallery minorGallery) {
        this.id = minorGallery.getGalleryId();
        this.createdAt = minorGallery.getCreatedAt();
        this.description = minorGallery.getDescription();
        this.name = minorGallery.getName();
        this.creator = minorGallery.getCreator();
    }
}
