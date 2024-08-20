package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.InquiryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {   //ysh
    List<InquiryAnswer> findByMember_Id(Long memberId); //문의 작성한 멤버 찾기
}