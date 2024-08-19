package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "Posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post { //준혁이랑 사용이 똑같은 도메인 보류

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id", updatable = false)//게시물 아이디
    private Long postId;

    @Column(name = "title", nullable = false)   //게시물 제목
    private String title;

    @Column(name = "content", nullable = false) //게시물 내용
    private String content;

    @CreatedDate
    @Column(name = "created_at")    //생성날짜
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")    //수정날짜
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery; // Gallery 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minor_gallery_id")
    private MinorGallery minorGallery; // MinorGallery 참조

    @ManyToOne
    @JoinColumn(name = "board_id")  //게시판 아이디
    private Board board;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)  // 작성자 회원번호
    private Member author;

    @Column(name = "views", nullable = false)   // 조회수
    private int views;

    @Column(name = "likes", nullable = false)   // 추천수
    private int likes;

    @Builder
    public Post(Gallery gallery, MinorGallery minorGallery, String title, String content, Board board, Member author) {
        this.gallery = gallery;
        this.minorGallery = minorGallery;
        this.title = title;
        this.content = content;
        this.board = board;
        this.author = author;
        this.views = 0;  // 기본값으로 초기화
        this.likes = 0;  // 기본값으로 초기화
        this.createdAt = LocalDateTime.now(); // 생성 시간 초기화
    }

//    public void update(String title, String content) {
//        this.title = title;
//        this.content = content;
//    }
}
