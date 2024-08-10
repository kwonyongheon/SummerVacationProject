package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.dto.CommentRequest;
import SummerVacationProject.HCI.web.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<Void> addComment(@RequestBody CommentRequest commentRequest) {
        commentService.addComment(commentRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    // 댓글 수정 엔드포인트 추가
    @PostMapping("/comment/{id}/edit")
    public ResponseEntity<Void> editComment(@PathVariable Long id, @RequestParam String content) {
        commentService.editComment(id, content);
        return ResponseEntity.ok().build();
    }
}
