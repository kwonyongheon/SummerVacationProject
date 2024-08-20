package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class GalleryService {   //보류
//    private final GalleryRepository galleryRepository;
//
//    @Autowired
//    public GalleryService(GalleryRepository galleryRepository) {
//        this.galleryRepository = galleryRepository;
//    }
//
//    public List<Gallery> getAllGalleries() {
//        return galleryRepository.findAll();
//    }
//
//    public Optional<Gallery> getGalleryById(Long id) {
//        return galleryRepository.findById(id);
//    }
//
//    public Gallery saveGallery(Gallery gallery) {
//        return galleryRepository.save(gallery);
//    }
//
//    public void deleteGallery(Long id) {
//        galleryRepository.deleteById(id);
//    }
//
//    public Gallery updateGallery(Long id, Gallery updatedGallery) {
//        if (galleryRepository.existsById(id)) {
//            updatedGallery.setId(id);
//            return galleryRepository.save(updatedGallery);
//        }
//        return null; // or throw exception
//    }
//
//    public Optional<Object> findById(Long galleryId) {
//        return null;
//    }
}
