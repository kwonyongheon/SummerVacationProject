package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MinorGalleryRepository extends JpaRepository<Board, Long> {

}