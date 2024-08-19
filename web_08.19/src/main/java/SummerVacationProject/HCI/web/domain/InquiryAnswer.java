package SummerVacationProject.HCI.web.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = "InquiryAnswers")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class InquiryAnswer {    //ysh

    // 답변 고유번호 (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "inquiry_answer_id", updatable = false)
    private Long inquiryAnswerId;

    // 문의 번호 (Foreign Key)
    @ManyToOne
    @JoinColumn(name = "inquiry_id", nullable = false)
    private Inquiry inquiry;

    // 작성자 번호 (Foreign Key)
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 답변 제목
    @Column(name = "title", nullable = false)  //1000자 제한
    private String title;

    // 답변 내용
    @Column(name = "content", nullable = false, length = 1000)  //1000자 제한
    private String content;

    // 답변 시간 (자동 생성)
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 생성자
    @Builder
    public InquiryAnswer(Inquiry inquiry, Member member, String title, String content) {
        this.inquiry = inquiry;
        this.member = member;
        this.content = content;
        this.title = title;
    }
}

