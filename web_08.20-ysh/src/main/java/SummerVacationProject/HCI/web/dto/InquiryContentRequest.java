package SummerVacationProject.HCI.web.dto;

import SummerVacationProject.HCI.web.domain.GalleryBoardType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryContentRequest {    //ysh
    private String inquiryType;  // 문의 종류 ("GALLERY_REQUEST" 또는 "OTHER_INQUIRY")
    // 공통 필드 (갤러리 이름 = 문의 제목, 갤러리 설명 = 문의 내용)
    private String title;        // 갤러리 이름 또는 문의 제목
    private String content;      // 갤러리 설명 또는 문의 내용

    // 갤러리 개설 요청 관련 필드
    private GalleryBoardType galleryBoardType;  // 갤러리 타입 (갤러리 개설 요청에만 해당)
}
