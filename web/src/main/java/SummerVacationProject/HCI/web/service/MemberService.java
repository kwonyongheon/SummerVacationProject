package SummerVacationProject.HCI.web.service;

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
        return memberRepository.save(Member.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .tagLine(dto.getTagLine())
                .gameName(dto.getGameName())
                .birth(dto.getBirth())
                .build()).getId();
    }


}
