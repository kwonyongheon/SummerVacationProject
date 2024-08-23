package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Recommendations_id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = true)
    private Member member;

    @Builder
    public Recommendation(Comment comment, Post post, Member member) {
        this.comment = comment;
        this.post = post;
        this.member = member;
    }
}
