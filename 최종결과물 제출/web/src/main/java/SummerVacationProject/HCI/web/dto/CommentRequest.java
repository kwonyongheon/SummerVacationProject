package SummerVacationProject.HCI.web.dto;

import SummerVacationProject.HCI.web.domain.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    private String content;
    private Long postId;
    private Long parentId; // 이 필드를 추가합니다.
    private Member author;

    // 확인용으로 사용
    @Override
    public String toString() {
        return "CommentRequest{" +
                "content='" + content + '\'' +
                ", postId=" + postId +
                ", parentId=" + parentId +
                ", author=" + author +
                '}';
    }
}
