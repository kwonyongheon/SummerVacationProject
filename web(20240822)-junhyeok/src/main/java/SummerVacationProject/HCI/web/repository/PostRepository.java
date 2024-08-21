package SummerVacationProject.HCI.web.repository;

import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByGalleryIdOrderByIdDesc(Long galleryId); // 특정 갤러리의 게시물을 ID 기준으로 내림차순 정렬하여 조회
    List<Post> findByGalleryIdOrderByRecommendCountDesc(Long galleryId); // 특정 갤러리의 게시물을 추천 수 기준으로 내림차순 정렬하여 조회
    List<Post> findByGalleryIdOrderByViewCountDesc(Long galleryId); // 특정 갤러리의 게시물을 조회 수 기준으로 내림차순 정렬하여 조회
    List<Post> findByGalleryIdAndAuthor(Long galleryId, Member author); // 특정 갤러리 내에서 특정 사용자가 작성한 게시물들을 조회
    List<Post> findByAuthorId(Long authorId); // 특정 사용자가 작성한 모든 게시물들을 조회
    List<Post> findByGalleryIdAndAuthorId(Long galleryId, Long authorId); // 특정 갤러리 내에서 특정 사용자가 작성한 게시물을 조회

}
