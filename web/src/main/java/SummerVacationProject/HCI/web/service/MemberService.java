package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.Exception.JoinException;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.AddMemberRequest;
import SummerVacationProject.HCI.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

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


}
