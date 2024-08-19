package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Inquiry;
import SummerVacationProject.HCI.web.domain.InquiryAnswer;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.InquiryAnswerRequest;
import SummerVacationProject.HCI.web.repository.InquiryAnswerRepository;
import SummerVacationProject.HCI.web.repository.InquiryRepository;
import SummerVacationProject.HCI.web.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdminService { //ysh

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InquiryRepository inquiryRepository;

    @Autowired
    private InquiryAnswerRepository inquiryAnswerRepository;

    //모든 사용자 조회 -ysh
    public List<Member> getAllUsers() {
        return memberRepository.findAll();
    }

    // 답변 저장 로직 추가
    public void saveInquiryAnswer(InquiryAnswerRequest answerRequest) {
        try {
            Inquiry inquiry = inquiryRepository.findById(answerRequest.getInquiryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry ID"));
            Member member = memberRepository.findById(answerRequest.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

            InquiryAnswer inquiryAnswer = new InquiryAnswer();
            inquiryAnswer.setInquiry(inquiry);
            inquiryAnswer.setMember(member);
            inquiryAnswer.setTitle(answerRequest.getTitle());
            inquiryAnswer.setContent(answerRequest.getContent());
            inquiryAnswer.setCreatedAt(LocalDateTime.now());


            // 데이터베이스 저장
            inquiryAnswerRepository.save(inquiryAnswer);
        } catch (Exception e) {
            // 로그를 추가하여 예외를 확인할 수 있습니다.
            e.printStackTrace();
            throw e; // 예외를 다시 던져서 외부에서 처리하도록 합니다.
        }
    }



}
