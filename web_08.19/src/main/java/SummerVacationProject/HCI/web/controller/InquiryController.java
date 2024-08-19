package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.GalleryBoardType;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.InquiryContentRequest;
import SummerVacationProject.HCI.web.service.InquiryService;
import SummerVacationProject.HCI.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor    //필수 생성자 자동 생성
@Controller
@RequestMapping("/inquiry")
public class InquiryController {    //ysh
    private final InquiryService inquiryService;

    @GetMapping
    public String inquiryForm(Model model) {
        model.addAttribute("galleryBoardTypes", GalleryBoardType.values()); //갤러리 항목에 GalleryBoardType enum 데이터 값
        return "inquiry";
    }

    @PostMapping("/gallerySend")
    public String inquiryFormSend(@ModelAttribute InquiryContentRequest request,
                                  @AuthenticationPrincipal Member member,
                                  Model model) {
        try {
            if ("GALLERY_REQUEST".equals(request.getInquiryType())) {
                inquiryService.createInquiry(request, member);
                model.addAttribute("status", "galleryRequestSuccess");
            } else if ("OTHER_INQUIRY".equals(request.getInquiryType())) {
                inquiryService.createOtherInquiry(request, member);
                model.addAttribute("status", "otherInquirySuccess");
            }
        } catch (Exception e) {
            if ("GALLERY_REQUEST".equals(request.getInquiryType())) {
                model.addAttribute("status", "galleryRequestFail");
            } else if ("OTHER_INQUIRY".equals(request.getInquiryType())) {
                model.addAttribute("status", "otherInquiryFail");
            }
        }
        model.addAttribute("galleryBoardTypes", GalleryBoardType.values()); // 성공/실패 여부와 함께 다시 갤러리 타입 전달
        return "inquiry";  // 메시지를 표시하기 위해 다시 inquiry 페이지로 이동
    }
}
