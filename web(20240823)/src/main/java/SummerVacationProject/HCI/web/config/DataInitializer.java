package SummerVacationProject.HCI.web.config;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import SummerVacationProject.HCI.web.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final MemberRepository memberRepository;
    private final GalleryRepository galleryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .email("example@example.com")
                .password(bCryptPasswordEncoder.encode("1"))
                .nickName("관리자1번")
                .tagLine("#")
                .gameName("#")
                .birth("0001-01-01")
                .admin(true)
                .build();

        Gallery freegallery = Gallery.builder()
                .creator(member)
                .name("자유게시판")
                .description("자유게시판입니다. 자유롭게 이용해주세요")
                .build();
        Gallery fungallery = Gallery.builder()
                .creator(member)
                .name("유머게시판")
                .description("유머게시판입니다.")
                .build();
        Gallery querygallery = Gallery.builder()
                .creator(member)
                .name("질문게시판")
                .description("질문게시판입니다.")
                .build();

        memberRepository.save(member);
        galleryRepository.save(freegallery);
        galleryRepository.save(fungallery);
        galleryRepository.save(querygallery);
    }
}
