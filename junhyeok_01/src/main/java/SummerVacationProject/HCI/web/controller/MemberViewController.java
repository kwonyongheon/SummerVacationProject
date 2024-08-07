package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
    public String Main() {
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
        }
        return "MemberInfo";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "AccessDenied";
    }
}
