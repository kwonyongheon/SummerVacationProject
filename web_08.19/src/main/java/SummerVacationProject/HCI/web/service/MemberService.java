package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.Exception.JoinException;
import SummerVacationProject.HCI.web.domain.InquiryAnswer;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.AddMemberRequest;
import SummerVacationProject.HCI.web.dto.UpdateMemberRequest;
import SummerVacationProject.HCI.web.repository.InquiryAnswerRepository;
import SummerVacationProject.HCI.web.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {    //용헌 코드
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final InquiryAnswerRepository inquiryAnswerRepository;


    private String currentAdminCode ="123456789";    //관리자 코드

    public Long save(AddMemberRequest dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new JoinException("이미 존재하는 이메일입니다.");
        } else if (memberRepository.existsByNickName(dto.getNickName())) {
            throw new JoinException("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .nickName(dto.getNickName())
                .tagLine(dto.getTagLine())
                .gameName(dto.getGameName())
                .birth(dto.getBirth())
                .admin(false)
                .build();

        return memberRepository.save(member).getId();
    }

    @Transactional
    public Member update(Long id, UpdateMemberRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        member.update(request.getNickName(), request.getGameName(), request.getTagLine(), request.getBirth());
        return member;
    }

    @Transactional
    public Member changePW(Long id, String password) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + id));

        member.changePW(bCryptPasswordEncoder.encode(password));
        return member;
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }



    // 관리자 코드 변경 메소드 -ysh
    @Transactional
    public void changeAdminCode(Long memberId, String adminCode) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);

        // 회원 찾기
        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }
        Member member = memberOptional.get();
        // 현재 관리자 코드 검증

        if (!currentAdminCode.equals(adminCode) || !currentAdminCode.equals("123456789")) {
            throw new IllegalStateException("관리자 코드가 일치하지 않습니다.");
        }
        // 관리자 코드 변경
        member.setAdmin(true);
        String newAdminCode = generateRandomCode();
        member.setAdminCode(newAdminCode); // 랜덤으로 새 코드 생성
        memberRepository.save(member);
        currentAdminCode = newAdminCode;

    }

    // ysh 랜덤 코드
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[24];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    // 답변 내역 조회 ysh
    @Transactional
    public List<InquiryAnswer> getAnswersByMemberId(Long memberId) {
        List<InquiryAnswer> answers = inquiryAnswerRepository.findByMember_Id(memberId);
        return answers;
    }



}
