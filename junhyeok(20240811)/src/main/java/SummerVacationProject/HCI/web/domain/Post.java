package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gallery_id", nullable = false)
    private Gallery gallery;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Builder.Default
    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Builder.Default
    @Column(name = "recommend_count")
    private Integer recommendCount = 0;

    @Column(name = "file_path")
    private String filePath;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
