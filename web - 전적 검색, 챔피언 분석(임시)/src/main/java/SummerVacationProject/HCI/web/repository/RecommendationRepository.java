package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Optional<Recommendation> findByMemberIdAndCommentId(Long memberId, Long commentId); // 특정 사용자가 특정 댓글에 대해 남긴 추천 정보를 조회
    Optional<Recommendation> findByMemberIdAndPostId(Long memberId, Long postId); // 특정 사용자가 특정 게시물에 대해 남긴 추천 정보를 조회
    void deleteByCommentId(Long commentId); // 특정 댓글에 대한 모든 추천 정보를 삭제
    void deleteByPostId(Long postId); // 특정 게시물에 대한 모든 추천 정보를 삭제
}
