package SummerVacationProject.HCI.web.dto;

import SummerVacationProject.HCI.web.domain.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequest {
    private Long galleryId;
    private String title;
    private String content;
    private Member author;
}
