package SummerVacationProject.HCI.web.dto;

import SummerVacationProject.HCI.web.domain.GalleryType;
import SummerVacationProject.HCI.web.domain.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddBoardRequest {  //보류
    private String title;   // 게시판 제목
    private String description; // 게시판 설명
    private LocalDateTime createdAt;
    private Member member;  // 회원 객체
    private GalleryType galleryType;    // 갤러리/마이너갤러리
}
