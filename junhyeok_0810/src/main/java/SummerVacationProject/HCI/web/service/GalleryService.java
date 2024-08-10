package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.GalleryRequest;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GalleryService {

    private final GalleryRepository galleryRepository;

    public List<Gallery> getGalleriesByMember(Member member) {
        return galleryRepository.findByMember(member);
    }

    public List<Gallery> getOtherGalleries(Member member) {
        return galleryRepository.findByMemberNot(member);
    }

    public Gallery getGalleryById(Long id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("갤러리를 찾을 수 없습니다."));
    }

    public Gallery createGallery(Member member, GalleryRequest galleryRequest) {
        if (galleryRepository.countByMember(member) >= 3) {
            throw new IllegalArgumentException("갤러리는 3개까지 생성할 수 있습니다.");
        }
        if (galleryRepository.existsByTitle(galleryRequest.getTitle())) {
            throw new IllegalArgumentException("이미 존재하는 갤러리 제목입니다.");
        }

        Gallery gallery = Gallery.builder()
                .member(member)
                .title(galleryRequest.getTitle())
                .description(galleryRequest.getDescription())
                .build();

        return galleryRepository.save(gallery);
    }

    public void deleteGallery(Member member, Long galleryId) {
        Gallery gallery = galleryRepository.findByIdAndMember(galleryId, member)
                .orElseThrow(() -> new IllegalArgumentException("갤러리를 찾을 수 없습니다."));
        galleryRepository.delete(gallery);
    }
}
