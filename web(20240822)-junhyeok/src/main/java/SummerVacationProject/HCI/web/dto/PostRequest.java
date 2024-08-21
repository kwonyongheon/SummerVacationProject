package SummerVacationProject.HCI.web.dto;

import SummerVacationProject.HCI.web.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequest {
    private Long galleryId; // 갤러리 ID 필드 추가
    private String title;
    private String content;
    private Member author;
    private Integer viewCount; // 조회수 필드 추가
    private Integer recommendCount; // 추천수 필드 추가
    private MultipartFile file; // 첨부파일 필드 추가
    private String filePath; // 파일 경로 필드 추가
}
