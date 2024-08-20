package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Inquiry;
import SummerVacationProject.HCI.web.domain.InquiryAnswer;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.InquiryAnswerRequest;
import SummerVacationProject.HCI.web.service.AdminService;
import SummerVacationProject.HCI.web.service.InquiryService;
import SummerVacationProject.HCI.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;    //스프링 웹소켓 설치 필요
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {  //ysh
    @Autowired
    private AdminService adminService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private InquiryService inquiryService;


    //ysh
    @GetMapping("/setting")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminMain(Model model) {
        // 필요한 데이터 모델 추가
        model.addAttribute("members", adminService.getAllUsers());
        model.addAttribute("inquirys", inquiryService.getAllInquiries());
        return "adminWebManagement"; // 템플릿 파일 이름
    }


    // 사용자 삭제 -ysh
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.ok("사용자가 삭제되었습니다.");
    }

    @GetMapping("/inquiry/{id}")
    @ResponseBody
    public ResponseEntity<Inquiry> getInquiryDetail(@PathVariable Long id) {
        Inquiry inquiry = inquiryService.getInquiryById(id);
        if (inquiry != null) {
            return ResponseEntity.ok(inquiry);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 특정 닉네임을 가진 사용자의 모든 문의 조회
    @GetMapping("/inquiries/search")
    @ResponseBody
    public ResponseEntity<List<Inquiry>> searchInquiriesByNickName(@RequestParam("nickname") String nickName) {
        List<Inquiry> inquiries = inquiryService.getInquiriesByNickName(nickName);
        return ResponseEntity.ok(inquiries);
    }

    // 갤러리 개설 요청 문의 조회
    @GetMapping("/inquiries/filterGallery")
    @ResponseBody
    public ResponseEntity<List<Inquiry>> getGalleryInquiries() {
        List<Inquiry> inquiries = inquiryService.getInquiriesByType("GALLERY_REQUEST");
        return ResponseEntity.ok(inquiries);
    }

    // 기타 문의 조회
    @GetMapping("/inquiries/filterOther")
    @ResponseBody
    public ResponseEntity<List<Inquiry>> getOtherInquiries() {
        List<Inquiry> inquiries = inquiryService.getInquiriesByType("OTHER_INQUIRY");
        return ResponseEntity.ok(inquiries);
    }

    // 모든 문의 조회
    @GetMapping("/inquiries/all")
    @ResponseBody
    public ResponseEntity<List<Inquiry>> getAllInquiries() {
        List<Inquiry> inquiries = inquiryService.getAllInquiries();
        return ResponseEntity.ok(inquiries);
    }

    // 관리자 답변 제출
    @PostMapping("/inquiries/{id}/reply")
    public ResponseEntity<String> submitInquiryAnswer(@PathVariable Long id, @RequestBody InquiryAnswerRequest answerRequest) {
        Inquiry inquiry = inquiryService.getInquiryById(id);  // 해당 문의 조회
        if (inquiry != null) {
            answerRequest.setInquiryId(id);  // 문의 ID 설정
            answerRequest.setMemberId(inquiry.getMember().getId());  // 문의 작성자의 ID 설정
            adminService.saveInquiryAnswer(answerRequest);
            return ResponseEntity.ok("답변이 성공적으로 저장되었습니다.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
