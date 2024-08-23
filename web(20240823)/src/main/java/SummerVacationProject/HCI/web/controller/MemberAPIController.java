package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.Exception.JoinException;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.AddMemberRequest;
import SummerVacationProject.HCI.web.dto.ChangePasswordRequest;
import SummerVacationProject.HCI.web.dto.CheckPasswordRequest;
import SummerVacationProject.HCI.web.dto.UpdateMemberRequest;
import SummerVacationProject.HCI.web.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class MemberAPIController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

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
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody UpdateMemberRequest request) {
        Member updateMember = memberService.update(id, request);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authentication newAuth = new UsernamePasswordAuthenticationToken(updateMember, authentication.getCredentials(), authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return ResponseEntity.ok(updateMember);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordRequest request,
                                                              HttpServletRequest httpServletRequest,
                                                              HttpServletResponse httpServletResponse) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "현재 비밀번호가 올바르지 않습니다."));
        }

        // 새 비밀번호가 현재 비밀번호와 같은지 확인
        if (passwordEncoder.matches(request.getNewPassword(), member.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "새 비밀번호는 기존 비밀번호와 달라야 합니다."));
        }

        // 새 비밀번호와 확인 비밀번호 일치 여부 확인
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다."));
        }

        // 비밀번호 변경 처리
        memberService.changePW(member.getId(), request.getNewPassword());

        // 로그아웃 처리
        new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);

        // 성공 응답 반환
        Map<String, String> response = new HashMap<>();
        response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-password")
    public ResponseEntity<Void> checkPassword(@RequestBody CheckPasswordRequest dto) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            // 비밀번호가 맞지 않으면 400 Bad Request 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 비밀번호가 맞으면 200 OK 반환
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/withDrawal/{id}")
    public ResponseEntity<?> withdrawMember(@PathVariable Long id,
                                            HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            memberService.delete(id);
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, authentication);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"회원 탈퇴에 실패했습니다: " + e.getMessage() + "\"}");
        }
    }



}
