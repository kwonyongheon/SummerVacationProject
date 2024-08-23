package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Posts")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id", updatable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery; // Gallery 참조

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "minor_gallery_id")
    private MinorGallery minorGallery; // MinorGallery 참조

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = true)
    private Member author;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "recommend_count")
    private Integer recommendCount = 0;

    @Column(name = "file_path")
    private String filePath;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recommendation> recommendations;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Post(Gallery gallery, MinorGallery minorGallery, String title, String content, Member author, String filePath) {
        this.gallery = gallery;
        this.minorGallery = minorGallery;
        this.title = title;
        this.content = content;
        this.author = author;
        this.filePath = filePath;
    }

}
