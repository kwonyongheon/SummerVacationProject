package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import SummerVacationProject.HCI.web.dto.PostRequest;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import SummerVacationProject.HCI.web.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final GalleryRepository galleryRepository;

    public List<Post> getPostsByGalleryId(Long galleryId) {
        return postRepository.findByGalleryId(galleryId);
    }

    public List<Post> getPostsByGalleryIdAndAuthor(Long galleryId, Member author) {
        return postRepository.findByGalleryIdAndAuthor(galleryId, author);
    }

    @Transactional
    public Post createPost(PostRequest postRequest) {
        Gallery gallery = galleryRepository.findById(postRequest.getGalleryId())
                .orElseThrow(() -> new IllegalArgumentException("갤러리를 찾을 수 없습니다."));

        String filePath = postRequest.getFilePath();
        if (postRequest.getFile() != null && !postRequest.getFile().isEmpty()) {
            filePath = saveFile(postRequest.getFile());
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
            String uploadDirectory = System.getProperty("user.dir") + "/uploads/";
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDirectory, fileName);

            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }

            file.transferTo(filePath.toFile());

            return filePath.toString();
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
}
