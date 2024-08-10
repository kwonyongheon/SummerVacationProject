package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Comment;
import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import SummerVacationProject.HCI.web.dto.CommentRequest;
import SummerVacationProject.HCI.web.dto.PostRequest;
import SummerVacationProject.HCI.web.service.CommentService;
import SummerVacationProject.HCI.web.service.GalleryService;
import SummerVacationProject.HCI.web.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/gallery")
public class PostController {

    private final PostService postService;
    private final GalleryService galleryService;
    private final CommentService commentService;

    @GetMapping("/{galleryId}")
    public String getPosts(@PathVariable Long galleryId, @RequestParam(required = false) String sort, Model model) {
        Gallery gallery = galleryService.getGalleryById(galleryId);
        List<Post> posts;

        if ("recommend".equals(sort)) {
            posts = postService.getPostsByGalleryIdOrderByRecommendCountDesc(galleryId);
        } else if ("view".equals(sort)) {
            posts = postService.getPostsByGalleryIdOrderByViewCountDesc(galleryId);
        } else {
            posts = postService.getPostsByGalleryIdOrderByIdDesc(galleryId);
        }

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
    public String createPost(@PathVariable Long galleryId, @ModelAttribute PostRequest postRequest, @RequestParam("file") MultipartFile file) {
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
            postService.createPost(postRequest, file); // MultipartFile 추가
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
        List<Comment> comments = post.getComments();
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "post";
    }

    @PostMapping("/post/{id}/recommend")
    public String recommendPost(@PathVariable Long id) {
        postService.incrementRecommendCount(id); // 추천수 증가
        return "redirect:/gallery/post/" + id;
    }

    @DeleteMapping("/post/{id}/delete")
    @ResponseBody
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/post/{postId}/comment")
    public String addComment(@PathVariable Long postId, @ModelAttribute CommentRequest commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        commentRequest.setAuthor(member);
        commentRequest.setPostId(postId);

        commentService.addComment(commentRequest);

        return "redirect:/gallery/post/" + postId;
    }

    @PostMapping("/post/comment/{commentId}/recommend")
    public String recommendComment(@PathVariable Long commentId) {
        commentService.incrementRecommendCount(commentId);
        Comment comment = commentService.getCommentById(commentId);
        return "redirect:/gallery/post/" + comment.getPost().getId();
    }

    @DeleteMapping("/post/comment/{commentId}/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        try {
            commentService.deleteComment(commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/post/comment/{commentId}/edit")
    @ResponseBody
    public ResponseEntity<Void> editComment(@PathVariable Long commentId, @RequestParam("content") String content) {
        try {
            commentService.editComment(commentId, content);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
