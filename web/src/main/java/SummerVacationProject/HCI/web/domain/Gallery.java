package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "galleries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gallery {

    @Id
    @Column(name = "gallery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 갤러리 ID (기본키)

    @Enumerated(EnumType.STRING)
    @Column(name = "gallery_Board_type", nullable = false)
    private GalleryBoardType galleryBoardType;  // 갤러리 게시판의 종류

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();  // 갤러리에 속한 게시판들

    @Builder
    public Gallery(Long id, GalleryBoardType galleryBoardType) {
        this.id = id;
        this.galleryBoardType = galleryBoardType;

    }
}
