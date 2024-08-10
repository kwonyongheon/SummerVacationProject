package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import SummerVacationProject.HCI.web.dto.PostRequest;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import SummerVacationProject.HCI.web.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final GalleryRepository galleryRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<Post> getPostsByGalleryIdOrderByIdDesc(Long galleryId) {
        return postRepository.findByGalleryIdOrderByIdDesc(galleryId);
    }

    public List<Post> getPostsByGalleryIdOrderByRecommendCountDesc(Long galleryId) {
        return postRepository.findByGalleryIdOrderByRecommendCountDesc(galleryId);
    }

    public List<Post> getPostsByGalleryIdOrderByViewCountDesc(Long galleryId) {
        return postRepository.findByGalleryIdOrderByViewCountDesc(galleryId);
    }

    public List<Post> getPostsByGalleryIdAndAuthor(Long galleryId, Member author) {
        return postRepository.findByGalleryIdAndAuthor(galleryId, author);
    }

    @Transactional
    public Post createPost(PostRequest postRequest, MultipartFile file) {
        Gallery gallery = galleryRepository.findById(postRequest.getGalleryId())
                .orElseThrow(() -> new IllegalArgumentException("갤러리를 찾을 수 없습니다."));

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = saveFile(file);
        }

        Post post = Post.builder()
                .gallery(gallery)
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .filePath(filePath) // 파일 경로 설정
                .viewCount(0) // 기본값 설정
                .recommendCount(0) // 기본값 설정
                .build();

        return postRepository.save(post);
    }

    private String saveFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String serverFilename = UUID.randomUUID() + "_" + fileName;
            Path filePath = Paths.get(uploadDir, serverFilename);

            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            file.transferTo(filePath.toFile());

            System.out.println("파일 저장 완료: " + filePath.toString());
            return serverFilename;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 오류", e);
        }
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
    }

    @Transactional
    public void incrementViewCount(Long id) {
        Post post = getPostById(id);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void incrementRecommendCount(Long id) {
        Post post = getPostById(id);
        post.setRecommendCount(post.getRecommendCount() + 1);
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
