package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.Exception.JoinException;
import SummerVacationProject.HCI.web.dto.AddMemberRequest;
import SummerVacationProject.HCI.web.service.MemberService;
import lombok.RequiredArgsConstructor;
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
}
