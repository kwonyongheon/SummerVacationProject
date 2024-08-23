package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("GALLERY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gallery extends BaseGallery {

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;
    
    @Builder
    public Gallery(Member creator, String name, String description) {
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
