package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Board;
import SummerVacationProject.HCI.web.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {   //보류

}
