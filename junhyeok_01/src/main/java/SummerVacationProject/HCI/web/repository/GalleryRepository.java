package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByMember(Member member);
    List<Gallery> findByMemberNot(Member member);
    int countByMember(Member member);
    boolean existsByTitle(String title);
    Optional<Gallery> findByIdAndMember(Long id, Member member);
}
