package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "Inquirys")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry {  //ysh

    // 문의 고유번호 (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inquiry_id", updatable = false)
    private Long id;

    // 회원번호 (Foreign Key)
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 문의 종류
    @Column(name = "type", nullable = false)
    private String type;

    // 갤러리 타입 (갤러리 개설 요청일 때만 사용)
    @Enumerated(EnumType.STRING)
    @Column(name = "gallery_board_type", nullable = true )
    private GalleryBoardType galleryBoardType;

    // 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 내용
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    // 문의 시간 (자동 생성)
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    // 생성자
    @Builder
    public Inquiry(Member member, String type, String title, String content, GalleryBoardType galleryBoardType) {
        this.member = member;
        this.type = type;
        this.title = title;
        this.content = content;
        this.galleryBoardType = galleryBoardType;
    }
}
