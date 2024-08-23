package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Comment;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import SummerVacationProject.HCI.web.domain.Post;
import SummerVacationProject.HCI.web.dto.GalleryListViewResponse;
import SummerVacationProject.HCI.web.dto.MGalleryListViewResponse;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import SummerVacationProject.HCI.web.repository.MinorGalleryRepository;
import SummerVacationProject.HCI.web.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberViewController {
    private final MinorGalleryService minorGalleryService;
    private final PostService postService;
    private final CommentService commentService;
    private final GalleryService galleryService;

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
    public String Main(Model model) {
        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();
        List<MGalleryListViewResponse> MinorGalleries = minorGalleryService.findall().stream()
                .map(MGalleryListViewResponse::new)
                .toList();
        model.addAttribute("MinorGalleries", MinorGalleries);
        model.addAttribute("Galleries", Galleries);
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
        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();
        model.addAttribute("Galleries", Galleries);
        return "MemberInfo";
    }

    @GetMapping("/myPage")  // myPage 구현 -ysh
    public String myPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        List<MinorGallery> galleries = minorGalleryService.getGalleriesByMember(member);
        List<Post> posts = postService.getPostsByMember(member);
        List<Comment> comments =  commentService.getCommentsByMember(member);
        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();
        model.addAttribute("Galleries", Galleries);

        model.addAttribute("member", member);
        model.addAttribute("galleries", galleries);
        model.addAttribute("posts", posts);
        model.addAttribute("comments", comments);

        // 기본 홈 페이지를 처리
        return "myPage"; // 홈 페이지 템플릿 반환
    }

    @GetMapping("/access-denied")
    public String accessDenied(@RequestParam("status") int statusCode, Model model) {
        model.addAttribute("statusCode", statusCode);
        return "AccessDenied";  // accessDenied.html 또는 .jsp 페이지로 포워드
    }

}
