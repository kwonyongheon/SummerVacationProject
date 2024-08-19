package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryAnswerRequest { //ysh
    private Long inquiryId; // 답변 고유 번호
    private Long memberId;  // 문의 작성자 ID 추가
    private String title;   // 답변 제목
    private String content; // 답변 내용
}
