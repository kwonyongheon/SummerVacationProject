package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.Exception.CreateException;
import SummerVacationProject.HCI.web.domain.*;
import SummerVacationProject.HCI.web.dto.*;
import SummerVacationProject.HCI.web.service.CommentService;
import SummerVacationProject.HCI.web.service.GalleryService;
import SummerVacationProject.HCI.web.service.MinorGalleryService;
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
@RequestMapping("/MinorGallery")
public class MinorGalleryAPIController {
    private final MinorGalleryService minorGalleryService;
    private final PostService postService;
    private final CommentService commentService;
    private final GalleryService galleryService;

    // 새로운 갤러리 생성
    @PostMapping("/create")
    @ResponseBody
    public String createGallery(@RequestBody MinorGalleryRequest Request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        try {
            minorGalleryService.createGallery(member, Request);
            return "ok";
        } catch (CreateException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/list")
    public String MinorgalleryList(Model model) {
        List<MGalleryListViewResponse> MinorGalleries = minorGalleryService.findall().stream()
                .map(MGalleryListViewResponse::new)
                .toList();
        model.addAttribute("MinorGalleries", MinorGalleries);
        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();
        model.addAttribute("Galleries", Galleries);

        return "MgalleryList";
    }

    @GetMapping("/{id}")
    public String Minorgallery(@PathVariable Long id, @RequestParam(required = false) String sort, Model model) {
        MinorGallery minorGallery = minorGalleryService.findById(id);
        List<Post> posts;

        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();

        if ("recommend".equals(sort)) {
            posts = postService.getPostsByGalleryIdOrderByRecommendCountDesc(id);
        } else if ("view".equals(sort)) {
            posts = postService.getPostsByGalleryIdOrderByViewCountDesc(id);
        } else {
            posts = postService.getPostsByGalleryIdOrderByIdDesc(id);
        }

        model.addAttribute("Galleries", Galleries);
        model.addAttribute("Mgallery", minorGallery);
        model.addAttribute("posts", posts);
        return "Mgallery";
    }

    @GetMapping("/{galleryId}/create-post")
    public String CreatePostForm(@PathVariable Long galleryId, Model model) {
        MinorGallery minorGallery = minorGalleryService.findById(galleryId);
        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();
        model.addAttribute("Galleries", Galleries);
        model.addAttribute("minorGallery", minorGallery);
        return "Mpost-create";
    }

    @PostMapping("/{galleryId}/create-post")
    public String CreatePost(@PathVariable Long galleryId, @ModelAttribute PostRequest postRequest, @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        postRequest.setAuthor(member);
        postRequest.setGalleryId(galleryId);
        try {
            postService.createMPost(postRequest, file);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/MinorGallery/" + galleryId + "/create-post?error=true";
        }
        return "redirect:/MinorGallery/" + galleryId + "/create-post?create=true&galleryId=" + galleryId;
    }

    @GetMapping("/post/{postId}")
    public String getPostById(@PathVariable Long postId, Model model) {
        Post post = postService.getPostById(postId);
        MinorGallery minorGallery = post.getMinorGallery();
        List<Post> posts = postService.getPostsByGalleryIdOrderByIdDesc(minorGallery.getGalleryId());
        postService.incrementViewCount(postId); // 조회수 증가
        List<Comment> comments = post.getComments();

        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();
        model.addAttribute("Galleries", Galleries);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthor = false;

        if (authentication != null && authentication.getPrincipal() instanceof Member currentUser) {
            if (post.getAuthor() != null) {
                isAuthor = post.getAuthor().getId().equals(currentUser.getId());
            }
        }

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("isAuthor", isAuthor);  // 작성자 여부 추가
        model.addAttribute("Mgallery", minorGallery);
        model.addAttribute("posts", posts);

        return "post";
    }

    // 게시물 추천
    @PostMapping("/post/{id}/recommend")
    public String recommendPost(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        postService.toggleRecommendation(id, member.getId()); // 추천 토글
        return "redirect:/MinorGallery/post/" + id;
    }

    // 게시물 수정
    @PostMapping("/post/{id}/edit")
    public String editPost(@PathVariable Long id, @ModelAttribute PostRequest postRequest, @RequestParam("file") MultipartFile file) {
        postService.updatePost(id, postRequest, file);
        return "redirect:/MinorGallery/post/" + id;
    }

    // 게시물 삭제
    @PostMapping("/post/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        // 게시글의 갤러리 ID를 먼저 가져옵니다.
        Post post = postService.getPostById(id);
        Long galleryId = post.getMinorGallery().getGalleryId();

        // 게시글을 삭제합니다.
        postService.deletePost(id);

        // 삭제 후 갤러리 목록 페이지로 리다이렉트합니다.
        return "redirect:/MinorGallery/" + galleryId;
    }

    @PostMapping("/post/{postId}/comment")
    public String addComment(@PathVariable Long postId, @ModelAttribute CommentRequest commentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        commentRequest.setAuthor(member);
        commentRequest.setPostId(postId);

        commentService.addComment(commentRequest);

        return "redirect:/MinorGallery/post/" + postId;
    }

    // 댓글 추천
    @PostMapping("/post/comment/{commentId}/recommend")
    public String recommendComment(@PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        commentService.toggleRecommendation(commentId, member.getId());
        Comment comment = commentService.getCommentById(commentId);
        return "redirect:/MinorGallery/post/" + comment.getPost().getPostId();
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

    @PutMapping("/modify/{id}")
    @ResponseBody
    public ResponseEntity<String> modifyGallery(@PathVariable Long id, @RequestBody EditGalleryRequest request) {
        // 갤러리 ID와 수정할 데이터를 이용해 갤러리 수정
        boolean isUpdated = minorGalleryService.updateGallery(id, request);
        if (isUpdated) {
            return ResponseEntity.ok("갤러리가 성공적으로 수정되었습니다.");
        } else {
            return ResponseEntity.status(404).body("갤러리를 찾을 수 없습니다.");
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteGallery(@PathVariable Long id) {
        try {
            boolean isDeleted = minorGalleryService.deleteGallery(id);

            if (isDeleted) {
                return ResponseEntity.ok("갤러리가 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("갤러리를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("갤러리 삭제 중 오류가 발생했습니다.");
        }
    }




}
