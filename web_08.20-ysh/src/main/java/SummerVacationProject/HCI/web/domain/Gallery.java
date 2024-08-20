package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "galleries")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "gallery_type")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Gallery { // 테이블 이름은 BaseGallery(용헌) -> Gallery(나)

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "gallery_id", updatable = false)
    private Long galleryId;

    @Column(name = "name", nullable = false, unique = true)
    protected String name;

    @Column(name = "description", length = 500)
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    protected Member creator;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void changeOwner(Member creator) {
        this.creator = creator;
    }

}

//
//@Entity
//@Table(name = "galleries")
//@Getter
//@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Gallery {
//
//    @Id
//    @Column(name = "gallery_id")    // 갤러리 ID (기본키)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Enumerated(EnumType.STRING)
//    @Column(name = "gallery_Board_type", nullable = false)  // 갤러리 게시판의 종류
//    private GalleryBoardType galleryBoardType;
//
//    @CreatedDate
//    @Column(name = "created_at", updatable = false) //생성 날짜
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at")     //수정날짜
//    private LocalDateTime updatedAt;
//
//    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Board> boards = new ArrayList<>();  // 갤러리에 속한 게시판들
//
//    @Builder
//    public Gallery(Long id) {
//        this.id = id;
//
//    }
//}
