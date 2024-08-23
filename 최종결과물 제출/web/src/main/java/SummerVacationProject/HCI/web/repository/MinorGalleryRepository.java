package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinorGalleryRepository extends JpaRepository<MinorGallery, Long> {
    int countByCreator(Member creator);
    boolean existsByName(String Name);
    List<MinorGallery> findByCreator(Member creator);
}
