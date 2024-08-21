package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
    List<Gallery> findByMember(Member member); // 특정 회원이 생성한 갤러리 목록 조회
    List<Gallery> findByMemberNot(Member member); // 특정 회원이 생성하지 않은 갤러리 목록 조회
    int countByMember(Member member); // 특정 회원이 생성한 갤러리 개수 조회
    boolean existsByTitle(String title); // 갤러리 제목이 이미 존재하는지 확인
    Optional<Gallery> findByIdAndMember(Long id, Member member); // 특정 회원이 생성한 특정 갤러리 조회
}
