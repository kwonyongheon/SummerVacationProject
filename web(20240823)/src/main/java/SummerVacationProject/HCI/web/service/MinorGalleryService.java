package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.Exception.CreateException;
import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import SummerVacationProject.HCI.web.dto.EditGalleryRequest;
import SummerVacationProject.HCI.web.dto.MinorGalleryRequest;
import SummerVacationProject.HCI.web.repository.MinorGalleryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MinorGalleryService {

    private final MinorGalleryRepository minorGalleryRepository;

    @Transactional
    public boolean updateGallery(Long id, EditGalleryRequest request) {
        MinorGallery minorGallery = minorGalleryRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("no gallery"));

        minorGallery.update(request.getName(), request.getDescription());
        try {
            minorGalleryRepository.save(minorGallery);
            return true;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public boolean deleteGallery(Long id) {
        Optional<MinorGallery> minorGalleryOptional = minorGalleryRepository.findById(id);

        if (minorGalleryOptional.isPresent()) {
            minorGalleryRepository.deleteById(id);
            return true;
        } else {
            return false; // 갤러리를 찾을 수 없는 경우
        }
    }

    public Long createGallery(Member member, MinorGalleryRequest request) {
        if (minorGalleryRepository.countByCreator(member) >= 3) {
            throw new CreateException("갤러리는 3개까지 생성할 수 있습니다.");
        }
        if (minorGalleryRepository.existsByName(request.getName())) {
            throw new CreateException("이미 존재하는 갤러리 제목입니다.");
        }

        MinorGallery gallery = MinorGallery.builder()
                .creator(member)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return minorGalleryRepository.save(gallery).getGalleryId();
    }

    public List<MinorGallery> findall() {
        return minorGalleryRepository.findAll();
    }

    public MinorGallery findById(Long id) {
        return minorGalleryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("갤러리를 찾을 수 없습니다. 관리자에게 문의하세요."));
    }

    public List<MinorGallery> getGalleriesByMember(Member member) {
        return minorGalleryRepository.findByCreator(member);
    }

}
