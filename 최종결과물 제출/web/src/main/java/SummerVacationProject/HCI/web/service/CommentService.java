package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Comment;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Recommendation;
import SummerVacationProject.HCI.web.dto.CommentRequest;
import SummerVacationProject.HCI.web.repository.CommentRepository;
import SummerVacationProject.HCI.web.repository.MemberRepository;
import SummerVacationProject.HCI.web.repository.PostRepository;
import SummerVacationProject.HCI.web.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private static final Logger logger = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final RecommendationRepository recommendationRepository;
    private final PostRepository postRepository; // PostRepository 추가
    private final MemberRepository memberRepository; // 추가된 부분
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 인코더 추가

    // 댓글 추가
    @Transactional
    public void addComment(CommentRequest commentRequest) {
        Comment parentComment = null;
        if (commentRequest.getParentId() != null && commentRequest.getParentId() > 0) {
            parentComment = commentRepository.findById(commentRequest.getParentId()).orElse(null);
        }

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .author(commentRequest.getAuthor())
                .post(postRepository.findById(commentRequest.getPostId()).orElseThrow()) // postRepository 사용
                .parent(parentComment)
                .build();

        commentRepository.save(comment);

        if (parentComment != null) {
            logger.info("대댓글이 작성되었습니다. 대댓글 ID: {}, 부모 댓글 ID: {}", comment.getId(), parentComment.getId());
        } else {
            logger.info("댓글이 작성되었습니다. 댓글 ID: {}", comment.getId());
        }
    }

    // 댓글 삭제
    @Transactional
    public boolean deleteComment(Long commentId, String password) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 비밀번호 비교
        if (!passwordEncoder.matches(password, comment.getAuthor().getPassword())) {
            logger.warn("비밀번호가 틀립니다. 댓글 ID: {}", commentId);
            return false;
        }

        // 댓글에 대한 추천 삭제
        recommendationRepository.deleteByCommentId(commentId);

        // 대댓글 및 그 추천 삭제
        deleteCommentAndReplies(comment);

        logger.info("댓글이 삭제되었습니다. 댓글 ID: {}", commentId);
        return true;
    }

    private void deleteCommentAndReplies(Comment comment) {
        // 대댓글에 대한 추천 삭제
        if (comment.getReplies() != null) {
            for (Comment reply : comment.getReplies()) {
                recommendationRepository.deleteByCommentId(reply.getId());
                deleteCommentAndReplies(reply);
            }
        }

        // 최종적으로 댓글 삭제
        commentRepository.delete(comment);
    }

    // 댓글 수정
    @Transactional
    public boolean editComment(Long commentId, String content, String password) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        logger.info("저장된 비밀번호: {}", comment.getAuthor().getPassword());
        logger.info("입력된 비밀번호: {}", password);

        // 비밀번호 비교
        if (!passwordEncoder.matches(password, comment.getAuthor().getPassword())) {
            logger.warn("비밀번호가 틀립니다. 댓글 ID: {}", commentId);
            return false;
        }

        comment.setContent(content);
        commentRepository.save(comment);
        logger.info("댓글이 수정되었습니다. 댓글 ID: {}, 내용: {}", commentId, content);
        return true;
    }

    // 댓글 추천 토글
    @Transactional
    public void toggleRecommendation(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        Optional<Recommendation> existingRecommendation =
                recommendationRepository.findByMemberIdAndCommentId(memberId, commentId);

        if (existingRecommendation.isPresent()) {
            // 기존 추천을 취소 (삭제)
            recommendationRepository.delete(existingRecommendation.get());
            comment.setRecommendCount(comment.getRecommendCount() - 1);
            logger.info("추천을 취소했습니다. commentId: {}, memberId: {}", commentId, memberId);
        } else {
            // 새로운 추천 추가
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

            Recommendation recommendation = Recommendation.builder()
                    .comment(comment)
                    .member(member)
                    .build();
            recommendationRepository.save(recommendation);
            comment.setRecommendCount(comment.getRecommendCount() + 1);
            logger.info("새로운 추천을 추가했습니다. commentId: {}, memberId: {}", commentId, memberId);
        }

        commentRepository.save(comment);
    }

    // 특정 댓글 조회
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    public List<Comment> getCommentsByMember(Member member) {
        return commentRepository.findByAuthor(member);
    }
}

