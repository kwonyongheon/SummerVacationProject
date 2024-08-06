package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.Exception.JoinException;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.AddMemberRequest;
import SummerVacationProject.HCI.web.dto.UpdateMemberRequest;
import SummerVacationProject.HCI.web.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;

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

    @PutMapping("/modify/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody UpdateMemberRequest request, Principal principal) {
        Member updateMember = memberService.update(id, request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updateMember, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return ResponseEntity.ok(updateMember);
    }


}
