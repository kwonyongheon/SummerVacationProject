package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {   //ysh

    // 특정 닉네임을 가진 사용자의 문의를 검색
    List<Inquiry> findByMember_NickName(String nickName);

    // 특정 문의 종류 찾기
    List<Inquiry> findByType(String type);
}
