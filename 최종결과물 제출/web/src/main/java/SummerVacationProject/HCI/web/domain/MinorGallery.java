package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("MINOR_GALLERY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MinorGallery extends BaseGallery {

    @OneToMany(mappedBy = "minorGallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @Builder
    public MinorGallery(Member creator, String name, String description) {
        super();
        this.creator = creator;
        this.name = name;
        this.description = description;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }

}