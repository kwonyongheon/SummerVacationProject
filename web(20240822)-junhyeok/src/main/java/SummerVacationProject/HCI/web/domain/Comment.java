package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 부모가 대댓글을 가질 수 있도록 사용 (부모 - 대댓글1, 대댓글2, ...)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    @Column(name = "content", nullable = false)
    private String content;

    @Builder.Default
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    @Column(name = "recommend_count")
    private Integer recommendCount = 0;
}
