package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {    //준혁 공통 사용 코드

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")  // 게시판 고유번호
    private Long id;

    @Column(name = "title", nullable = false)   // 게시판 이름
    private String title;

    @Column(name = "description")   // 게시판 설명
    private String description;

    @CreatedDate
    @Column(name = "created_at", updatable = false) // 게시판 생성 날짜
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "gallery_type", nullable = false)    // 게시판/마이너갤러리 선택
    private GalleryType galleryType;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)    // 회원 번호(FK)
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true) //  게시판/게시물 1:n 관계를 가짐
    private List<Post> posts = new ArrayList<>();


    //독립된 테이블 Gallery, MinorGallery를 한개의 board 테이블에 연결시키기 위해 두개의 갤러리,마이너갤러리 외래키를 참조함
    @ManyToOne
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;  // Gallery와 연결 (nullable = true로 설정할 수 있음)

    @ManyToOne
    @JoinColumn(name = "minor_gallery_id")
    private MinorGallery minorGallery;  // MinorGallery와 연결 (nullable = true로 설정할 수 있음)

    @Builder
    public Board(Long id, String title, String description, LocalDateTime createdAt, GalleryType galleryType, Member member, Gallery gallery, MinorGallery minorGallery, List<Post> posts) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.galleryType = galleryType;
        this.member = member;
        this.gallery = gallery;
        this.minorGallery = minorGallery;
        this.posts = posts;
    }
}
