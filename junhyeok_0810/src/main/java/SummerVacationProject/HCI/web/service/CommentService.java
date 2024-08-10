package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Comment;
import SummerVacationProject.HCI.web.domain.Post;
import SummerVacationProject.HCI.web.dto.CommentRequest;
import SummerVacationProject.HCI.web.repository.CommentRepository;
import SummerVacationProject.HCI.web.repository.PostRepository;
import SummerVacationProject.HCI.web.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final RecommendationRepository recommendationRepository;
    private final PostRepository postRepository;

    @Transactional
    public void addComment(CommentRequest commentRequest) {
        Comment parentComment = null;
        if (commentRequest.getParentId() != null && commentRequest.getParentId() > 0) {
            parentComment = commentRepository.findById(commentRequest.getParentId()).orElse(null);
        }

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .createdDate(LocalDateTime.now())
                .author(commentRequest.getAuthor())
                .post(postRepository.findById(commentRequest.getPostId()).orElseThrow())
                .parent(parentComment)
                .build();

        commentRepository.save(comment);

        if (parentComment != null) {
            logger.info("대댓글이 작성되었습니다. 대댓글 ID: {}, 부모 댓글 ID: {}", comment.getId(), parentComment.getId());
        } else {
            logger.info("댓글이 작성되었습니다. 댓글 ID: {}", comment.getId());
        }
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        deleteCommentAndReplies(comment);
        logger.info("댓글이 삭제되었습니다. 댓글 ID: {}", commentId);
    }

    private void deleteCommentAndReplies(Comment comment) {
        // Delete recommendations for this comment
        recommendationRepository.deleteByCommentId(comment.getId());

        // Recursively delete replies and their recommendations
        if (comment.getReplies() != null) {
            for (Comment reply : comment.getReplies()) {
                deleteCommentAndReplies(reply);
            }
        }

        // Finally delete the comment itself
        commentRepository.delete(comment);
    }

    @Transactional
    public void incrementRecommendCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setRecommendCount(comment.getRecommendCount() + 1);
        commentRepository.save(comment);
        logger.info("댓글 추천수가 증가했습니다. 댓글 ID: {}, 추천수: {}", commentId, comment.getRecommendCount());
    }

    @Transactional
    public void editComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setContent(content);
        commentRepository.save(comment);
        logger.info("댓글이 수정되었습니다. 댓글 ID: {}, 내용: {}", commentId, content);
    }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }
}
