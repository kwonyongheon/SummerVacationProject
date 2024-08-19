package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.GalleryBoardType;
import SummerVacationProject.HCI.web.domain.Inquiry;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.InquiryContentRequest;
import SummerVacationProject.HCI.web.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InquiryService {   //ysh

    private final InquiryRepository inquiryRepository;

    // 갤러리 개설 요청 내용
    @Transactional
    public void createInquiry(InquiryContentRequest request, Member member) {
        Inquiry inquiry = Inquiry.builder()
                .member(member)
                .type(request.getInquiryType())
                .title(request.getTitle())       // 공통 필드
                .content(request.getContent())   // 공통 필드
                .galleryBoardType(request.getGalleryBoardType())  // 갤러리 개설 요청에만 해당
                .build();
        inquiryRepository.save(inquiry);
    }

    // 기타 문의
    @Transactional
    public void createOtherInquiry(InquiryContentRequest request, Member member) {
        Inquiry inquiry = Inquiry.builder()
                .member(member)
                .type(request.getInquiryType())
                .title(request.getTitle())       // 공통 필드
                .content(request.getContent())   // 공통 필드
                .build();
        inquiryRepository.save(inquiry);
    }


    // 특정 닉네임을 가진 사용자의 모든 문의를 조회
    @Transactional(readOnly = true) //readOnly : 이 데이터는 변경, 삭제, 업데이트 할 수 없음
    public List<Inquiry> getInquiriesByNickName(String nickName) {
        return inquiryRepository.findByMember_NickName(nickName);
    }

    // 모든 문의 내용 검색
    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();
    }

    // 특정 문의 번호 조회
    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id).orElse(null);
    }

    // 문의 종류 조회
    public List<Inquiry> getInquiriesByType(String type) {
        return inquiryRepository.findByType(type);
    }


}
