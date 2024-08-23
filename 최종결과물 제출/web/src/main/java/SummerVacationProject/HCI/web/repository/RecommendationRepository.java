package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    Optional<Recommendation> findByMemberIdAndPostPostId(Long memberId, Long postId); // 특정 사용자가 특정 게시물에 대해 남긴 추천 정보를 조회
    void deleteByCommentId(Long commentId);

    Optional<Recommendation> findByMemberIdAndCommentId(Long memberId, Long commentId);
}
