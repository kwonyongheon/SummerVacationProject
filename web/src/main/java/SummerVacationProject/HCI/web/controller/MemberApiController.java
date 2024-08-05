package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.Exception.JoinException;
import SummerVacationProject.HCI.web.dto.AddMemberRequest;
import SummerVacationProject.HCI.web.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/join")
    public String join(AddMemberRequest dto, RedirectAttributes redirectAttributes) {
        try {
            memberService.save(dto);
            redirectAttributes.addAttribute("success", true);
        } catch (JoinException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/join";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
