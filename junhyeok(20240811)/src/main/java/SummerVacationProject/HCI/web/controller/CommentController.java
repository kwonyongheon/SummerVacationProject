package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.CommentRequest;
import SummerVacationProject.HCI.web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // PostController에 댓글, 대댓글 - (추가, 추천, 수정, 삭제) 작성되어있음 (초반에 안나누고 하다가 게시물에 다 적어버림)
    // 댓글 추가
    @PostMapping("/comment")
    public ResponseEntity<Void> addComment(@RequestBody CommentRequest commentRequest) {
        commentService.addComment(commentRequest);
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestParam String password) {
        boolean isDeleted = commentService.deleteComment(id, password);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 비밀번호가 틀린 경우 401 상태 반환
        }
    }

    // 댓글 수정
    @PostMapping("/{id}/edit")
    public ResponseEntity<Void> editComment(@PathVariable Long id, @RequestParam String content, @RequestParam String password) {
        boolean isUpdated = commentService.editComment(id, content, password);
        if (isUpdated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 비밀번호가 틀린 경우 401 상태 반환
        }
    }

    // 댓글 추천
    @PostMapping("/{id}/recommend")
    public ResponseEntity<Void> toggleRecommendation(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        commentService.toggleRecommendation(id, member.getId());
        return ResponseEntity.ok().build();
    }
}
