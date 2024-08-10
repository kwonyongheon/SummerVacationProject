package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    void deleteByCommentId(Long commentId);
}
