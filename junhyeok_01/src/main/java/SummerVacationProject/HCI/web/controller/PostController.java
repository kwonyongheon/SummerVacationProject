package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import SummerVacationProject.HCI.web.dto.PostRequest;
import SummerVacationProject.HCI.web.service.GalleryService;
import SummerVacationProject.HCI.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/gallery")
public class PostController {

    private final PostService postService;
    private final GalleryService galleryService;

    @GetMapping("/{galleryId}")
    public String getPosts(@PathVariable Long galleryId, Model model) {
        Gallery gallery = galleryService.getGalleryById(galleryId);
        List<Post> posts = postService.getPostsByGalleryId(galleryId);
        model.addAttribute("gallery", gallery);
        model.addAttribute("posts", posts);
        return "gallery-posts";
    }

    @GetMapping("/{galleryId}/my-posts")
    public String getMyPosts(@PathVariable Long galleryId, Model model) {
        Gallery gallery = galleryService.getGalleryById(galleryId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        List<Post> posts = postService.getPostsByGalleryIdAndAuthor(galleryId, member);
        model.addAttribute("gallery", gallery);
        model.addAttribute("posts", posts);
        return "my-posts";
    }

    @GetMapping("/{galleryId}/create-post")
    public String createPostForm(@PathVariable Long galleryId, Model model) {
        model.addAttribute("galleryId", galleryId);
        return "create-post";
    }

    @PostMapping("/{galleryId}/create-post")
    public String createPost(@PathVariable Long galleryId, @ModelAttribute PostRequest postRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        postRequest.setAuthor(member);
        postRequest.setGalleryId(galleryId);

        // 갤러리 ID가 유효한지 확인
        Gallery gallery = galleryService.getGalleryById(galleryId);
        if (gallery == null) {
            return "redirect:/gallery/" + galleryId + "/create-post?error=invalid_gallery";
        }

        try {
            postService.createPost(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/gallery/" + galleryId + "/create-post?error=true";
        }

        return "redirect:/gallery/" + galleryId;
    }

    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        postService.incrementViewCount(id); // 조회수 증가
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping("/post/{id}/recommend")
    public String recommendPost(@PathVariable Long id) {
        postService.incrementRecommendCount(id); // 추천수 증가
        return "redirect:/gallery/post/" + id;
    }
}
