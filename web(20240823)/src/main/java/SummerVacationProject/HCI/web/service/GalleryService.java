package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.Exception.CreateException;
import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import SummerVacationProject.HCI.web.dto.EditGalleryRequest;
import SummerVacationProject.HCI.web.dto.GalleryRequest;
import SummerVacationProject.HCI.web.dto.MinorGalleryRequest;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GalleryService {
    private final GalleryRepository galleryRepository;

    @Transactional
    public boolean updateGallery(Long id, EditGalleryRequest request) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("no gallery"));

        gallery.update(request.getName(), request.getDescription());
        try {
            galleryRepository.save(gallery);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deleteGallery(Long id) {
        Optional<Gallery> GalleryOptional = galleryRepository.findById(id);

        if (GalleryOptional.isPresent()) {
            galleryRepository.deleteById(id);
            return true;
        } else {
            return false; // 갤러리를 찾을 수 없는 경우
        }
    }

    public Long createGallery(Member member, GalleryRequest request) {

        Gallery gallery = Gallery.builder()
                .creator(member)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return galleryRepository.save(gallery).getGalleryId();
    }

    public List<Gallery> findall() {
        return galleryRepository.findAll();
    }

    public Gallery findById(Long id) {
        return galleryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("갤러리를 찾을 수 없습니다. 관리자에게 문의하세요."));
    }

    public List<Gallery> getGalleriesByMember(Member member) {
        return galleryRepository.findByCreator(member);
    }
}
