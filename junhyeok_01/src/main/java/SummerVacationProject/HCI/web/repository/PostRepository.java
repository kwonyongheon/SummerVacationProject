package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByGalleryId(Long galleryId);
    List<Post> findByGalleryIdAndAuthor(Long galleryId, Member author);
}
