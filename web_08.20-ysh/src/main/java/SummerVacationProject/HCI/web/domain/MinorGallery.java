package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MINOR_GALLERY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MinorGallery extends Gallery{  //보류

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "minor_gallery_id")
//    private Long id;  // 마이너 갤러리 ID (기본키)
//
//    @Column(name = "gallery_board_type", nullable = false)
//    private GalleryBoardType galleryBoardType;  // 갤러리 게시판의 종류
//
//    @ManyToOne
//    @JoinColumn(name = "member_id", nullable = false)   //회원번호, 개설 요청회원
//    private Member member;
//
//    @OneToMany(mappedBy = "minorGallery", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Board> boards = new ArrayList<>();  // 마이너 갤러리에 속한 게시판들
//
//    @Builder
//    public MinorGallery(Long id, GalleryBoardType galleryBoardType, Member member) {
//        this.id = id;
//        this.galleryBoardType = galleryBoardType;
//        this.member = member;
//    }

    @Builder
    public MinorGallery(Member creator, String name, String description) {
        super();
        this.creator = creator;
        this.name = name;
        this.description = description;
    }
}

