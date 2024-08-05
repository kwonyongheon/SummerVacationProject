package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "minor_galleries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MinorGallery {

    @Id
    @Column(name = "minor_gallery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 마이너 갤러리 ID (기본키)

    @Column(name = "requestContent")    
    private String requestContent;  // 개설 요청 내용

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Member creator;

    @OneToMany(mappedBy = "minorGallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();  // 갤러리에 속한 게시판들



    @Builder
    public MinorGallery(Long id, Member creator, String requestContent) {
        this.id = id;
        this.creator = creator;
        this.requestContent = requestContent;
    }
}
