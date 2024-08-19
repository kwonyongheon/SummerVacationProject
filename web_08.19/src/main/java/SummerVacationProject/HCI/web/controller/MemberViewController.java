package SummerVacationProject.HCI.web.controller;
// 8/8 코드
import SummerVacationProject.HCI.web.domain.GalleryBoardType;
import SummerVacationProject.HCI.web.domain.InquiryAnswer;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemberViewController {
    private final MemberService memberService;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/login")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/index";
        }
        return "login";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @GetMapping("/index")
    public String main(Model model, Authentication authentication) {
        model.addAttribute("galleryBoardTypes", GalleryBoardType.values()); //갤러리 항목에 GalleryBoardType enum 데이터 값
        if (authentication != null) { //ysh 사용자의 권한 정보를 동적 제어
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            model.addAttribute("roles", userDetails.getAuthorities());
        }
        return "index";
    }

    @GetMapping("/member-info/{id}")
    public String memberInfo(Model model, @PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            Member member = (Member) authentication.getPrincipal();
            if(member.getId().equals(id)) {
                model.addAttribute("member", member);
            }

            if(member.isAdmin()){
                // 관리자인 경우 관리자 코드를 모델에 추가
                model.addAttribute("adminCode", member.getAdminCode());
            }
        }
        return "MemberInfo";
    }

    @GetMapping("/access-denied")
    public String accessDenied(@RequestParam("status") int statusCode, Model model) {
        model.addAttribute("statusCode", statusCode);
        return "AccessDenied";  // accessDenied.html 또는 .jsp 페이지로 포워드
    }

    // 답변 내역 사용자 전달 -ysh
    @GetMapping("/inquiries/reply/passing")
    @ResponseBody
    public List<InquiryAnswer> responses(@AuthenticationPrincipal Member member) {
        List<InquiryAnswer> answers = memberService.getAnswersByMemberId(member.getId());
        return answers; // JSON 형식으로 반환됨
    }

}


//package SummerVacationProject.HCI.web.controller;
//
//import SummerVacationProject.HCI.web.domain.GalleryBoardType;
//import SummerVacationProject.HCI.web.domain.Member;
//import SummerVacationProject.HCI.web.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.servlet.view.RedirectView;
//
//@RequiredArgsConstructor
//@Controller
//public class MemberViewController {
//    private final MemberService memberService;
//
//    @GetMapping("/test")
//    public String test() {
//        return "test";
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated() &&
//                !"anonymousUser".equals(authentication.getPrincipal())) {
//            return "redirect:/index";
//        }
//        return "login";
//    }
//
//    @GetMapping("/join")
//    public String join() {
//        return "join";
//    }
//
//    @GetMapping("/index")
//    public String main(Model model) {
//        model.addAttribute("galleryBoardTypes", GalleryBoardType.values()); //갤러리 항목에 GalleryBoardType enum 데이터 값
//        return "index";
//    }
//
//    @GetMapping("/member-info/{id}")
//    public String memberInfo(Model model, @PathVariable Long id) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
//            Member member = (Member) authentication.getPrincipal();
//            if(member.getId().equals(id)) {
//                model.addAttribute("member", member);
//            }
//        }
//        return "MemberInfo";
//    }
//
//    @GetMapping("/access-denied")
//    public String accessDenied() {
//        return "AccessDenied";
//    }
//}
