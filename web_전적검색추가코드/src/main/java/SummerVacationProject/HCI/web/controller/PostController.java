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

    // 특정 갤러리의 모든 게시물을 가져와서 정렬 기준에 따라 반환
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

    // 내 게시물
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

    // 게시물 생성 폼
    @GetMapping("/{galleryId}/create-post")
    public String createPostForm(@PathVariable Long galleryId, Model model) {
        model.addAttribute("galleryId", galleryId);
        return "create-post";
    }

    // 게시물 생성
    @PostMapping("/{galleryId}/create-post")
    public String createPost(@PathVariable Long galleryId, @ModelAttribute PostRequest postRequest, @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        postRequest.setAuthor(member);
        postRequest.setGalleryId(galleryId);

        Gallery gallery = galleryService.getGalleryById(galleryId);
        if (gallery == null) {
            return "redirect:/gallery/" + galleryId + "/create-post?error=invalid_gallery";
        }

        try {
            postService.createPost(postRequest, file);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/gallery/" + galleryId + "/create-post?error=true";
        }

        return "redirect:/gallery/" + galleryId;
    }

    // 특정 게시물의 상세 정보를 가져와서 반환 (조회수 증가 포함)
    @GetMapping("/post/{id}")
    public String getPostById(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        postService.incrementViewCount(id); // 조회수 증가
        List<Comment> comments = post.getComments();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member currentUser = (Member) authentication.getPrincipal();
        boolean isAuthor = post.getAuthor().getId().equals(currentUser.getId());

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("isAuthor", isAuthor);  // 작성자 여부 추가

        return "post";
    }

    // 게시물 수정 폼
    @GetMapping("/post/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "edit-post";  // 수정 페이지로 이동
    }

    // 게시물 수정
    @PostMapping("/post/{id}/edit")
    public String editPost(@PathVariable Long id, @ModelAttribute PostRequest postRequest, @RequestParam("file") MultipartFile file) {
        postService.updatePost(id, postRequest, file);
        return "redirect:/gallery/post/" + id;
    }

    // 게시물 추천
    @PostMapping("/post/{id}/recommend")
    public String recommendPost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        postService.toggleRecommendation(id, member.getId()); // 추천 토글
        return "redirect:/gallery/post/" + id;
    }

    // 게시물 삭제
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        // 게시글의 갤러리 ID를 먼저 가져옵니다.
        Post post = postService.getPostById(id);
        Long galleryId = post.getGallery().getId();

        // 게시글을 삭제합니다.
        postService.deletePost(id);

        // 삭제 후 갤러리 목록 페이지로 리다이렉트합니다.
        return "redirect:/gallery/" + galleryId;
    }

    // 특정 갤러리 내 사용자의 모든 게시물 삭제 메서드
    @PostMapping("/{galleryId}/delete-my-posts")
    public String deleteMyPostsInGallery(@PathVariable Long galleryId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        // 로그인한 사용자의 특정 갤러리 내 게시물만 삭제합니다.
        postService.deleteUserPostsInGallery(galleryId, member.getId());

        return "redirect:/gallery/" + galleryId; // 삭제 후 해당 갤러리 목록 페이지로 리다이렉트
    }

    // 내 게시물들 전체 삭제 메서드(회원 탈퇴 때 사용?)
    @PostMapping("/delete-my-posts")
    public String deleteMyPosts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        // 로그인한 사용자의 게시물만 삭제합니다.
        postService.deleteUserPosts(member.getId());

        return "redirect:/gallery"; // 삭제 후 갤러리 목록 페이지로 리다이렉트
    }


    // 전체 게시물 삭제 메서드(관리자용 삭제기능으로 사용?)
    @PostMapping("/delete-all-posts")
    public String deleteAllPosts() {
        postService.deleteAllPosts(); // 모든 게시물을 삭제하는 서비스 호출
        return "redirect:/gallery"; // 삭제 후 갤러리 목록 페이지로 리다이렉트
    }

    // 댓글, 대댓글 - (추가, 추천, 수정, 삭제) 리팩토링 못시킴
    // 댓글 추가
    @PostMapping("/post/{postId}/comment")
    public String addComment(@PathVariable Long postId, @ModelAttribute CommentRequest commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        commentRequest.setAuthor(member);
        commentRequest.setPostId(postId);

        commentService.addComment(commentRequest);

        return "redirect:/gallery/post/" + postId;
    }

    // 댓글 추천
    @PostMapping("/post/comment/{commentId}/recommend")
    public String recommendComment(@PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        commentService.toggleRecommendation(commentId, member.getId());
        Comment comment = commentService.getCommentById(commentId);
        return "redirect:/gallery/post/" + comment.getPost().getId();
    }

    // 댓글 삭제
    @DeleteMapping("/post/comment/{commentId}/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, @RequestParam("password") String password) {
        try {
            boolean isDeleted = commentService.deleteComment(commentId, password);
            if (isDeleted) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 댓글 수정
    @PostMapping("/post/comment/{commentId}/edit")
    @ResponseBody
    public ResponseEntity<Void> editComment(@PathVariable Long commentId, @RequestParam("content") String content, @RequestParam("password") String password) {
        try {
            boolean isEdited = commentService.editComment(commentId, content, password);
            if (isEdited) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
