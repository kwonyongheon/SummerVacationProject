package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMinorGalleryGalleryIdOrderByPostIdDesc(Long galleryId); // 특정 갤러리의 게시물을 ID 기준으로 내림차순 정렬하여 조회
    List<Post> findByMinorGalleryGalleryIdOrderByRecommendCountDesc(Long galleryId); // 특정 갤러리의 게시물을 추천 수 기준으로 내림차순 정렬하여 조회
    List<Post> findByMinorGalleryGalleryIdOrderByViewCountDesc(Long galleryId); // 특정 갤러리의 게시물을 조회 수 기준으로 내림차순 정렬하여 조회

    List<Post> findByGalleryGalleryIdOrderByPostIdDesc(Long galleryId); // 특정 갤러리의 게시물을 ID 기준으로 내림차순 정렬하여 조회
    List<Post> findByGalleryGalleryIdOrderByRecommendCountDesc(Long galleryId); // 특정 갤러리의 게시물을 추천 수 기준으로 내림차순 정렬하여 조회
    List<Post> findByGalleryGalleryIdOrderByViewCountDesc(Long galleryId); // 특정 갤러리의 게시물을 조회 수 기준으로 내림차순 정렬하여 조회
    List<Post> findByAuthor(Member member);
}
