package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Board;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import SummerVacationProject.HCI.web.repository.MinorGalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//보류
@Service
public class MinorGalleryService {  //보류
//    private final MinorGalleryRepository minorGalleryRepository;
//
//    @Autowired
//    public MinorGalleryService(MinorGalleryRepository minorGalleryRepository) {
//        this.minorGalleryRepository = minorGalleryRepository;
//    }
//
//    public List<Board> getAllMinorGalleries() {
//        return minorGalleryRepository.findAll();
//    }
//
//    public Optional<Board> getMinorGalleryById(Long id) {
//        return minorGalleryRepository.findById(id);
//    }
//
//
//    public void deleteMinorGallery(Long id) {
//        minorGalleryRepository.deleteById(id);
//    }
//
//    public MinorGallery updateMinorGallery(Long id, MinorGallery updatedMinorGallery) {
//        if (minorGalleryRepository.existsById(id)) {
//            updatedMinorGallery.setId(id);
//            return minorGalleryRepository.save(updatedMinorGallery);
//        }
//        return null; // or throw exception
//    }
}
