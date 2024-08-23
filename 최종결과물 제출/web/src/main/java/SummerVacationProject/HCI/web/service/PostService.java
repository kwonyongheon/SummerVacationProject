package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.*;
import SummerVacationProject.HCI.web.dto.PostRequest;
import SummerVacationProject.HCI.web.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final MinorGalleryRepository minorGalleryRepository;
    private final GalleryRepository galleryRepository;
    private final MemberRepository memberRepository;
    private final RecommendationRepository recommendationRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));
    }

    public List<Post> getPostsByMember(Member member) {
        return postRepository.findByAuthor(member);
    }

    @Transactional
    public void incrementViewCount(Long id) {
        Post post = getPostById(id);
        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);
    }

    public List<Post> getPostsByGalleryIdOrderByIdDesc(Long galleryId) {
        return postRepository.findByMinorGalleryGalleryIdOrderByPostIdDesc(galleryId);
    }

    public List<Post> getPostsByGalleryIdOrderByRecommendCountDesc(Long galleryId) {
        return postRepository.findByMinorGalleryGalleryIdOrderByRecommendCountDesc(galleryId);
    }

    public List<Post> getPostsByGalleryIdOrderByViewCountDesc(Long galleryId) {
        return postRepository.findByMinorGalleryGalleryIdOrderByViewCountDesc(galleryId);
    }

    public List<Post> getPostsOrderByIdDesc(Long galleryId) {
        return postRepository.findByGalleryGalleryIdOrderByPostIdDesc(galleryId);
    }

    public List<Post> getPostsOrderByRecommendCountDesc(Long galleryId) {
        return postRepository.findByGalleryGalleryIdOrderByRecommendCountDesc(galleryId);
    }

    public List<Post> getPostsOrderByViewCountDesc(Long galleryId) {
        return postRepository.findByGalleryGalleryIdOrderByViewCountDesc(galleryId);
    }

    @Transactional
    public Post createMPost(PostRequest postRequest, MultipartFile file) {
        MinorGallery minorGallery = minorGalleryRepository.findById(postRequest.getGalleryId())
                .orElseThrow(() -> new IllegalArgumentException("갤러리를 찾을 수 없습니다."));

        String filePath = null;
        if (file != null && !file.isEmpty()) {
            filePath = saveFile(file);
        }

        Post post = Post.builder()
                .minorGallery(minorGallery)
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .author(postRequest.getAuthor())
                .filePath(filePath)
                .build();

        return postRepository.save(post);
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
                .filePath(filePath)
                .build();

        return postRepository.save(post);
    }

    @Transactional
    public void toggleRecommendation(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + memberId));

        Optional<Recommendation> existingRecommendation = recommendationRepository.findByMemberIdAndPostPostId(memberId, postId);

        if (existingRecommendation.isPresent()) {
            post.setRecommendCount(post.getRecommendCount() - 1);
            recommendationRepository.delete(existingRecommendation.get());
        } else {
            post.setRecommendCount(post.getRecommendCount() + 1);
            Recommendation recommendation = Recommendation.builder()
                    .post(post)
                    .member(member)
                    .build();
            recommendationRepository.save(recommendation);
        }

        postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long id, PostRequest postRequest, MultipartFile file) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        // 기존 파일 삭제 로직 추가
        String oldFilePath = post.getFilePath();
        if (file != null && !file.isEmpty()) {
            // 새로운 파일이 업로드된 경우 기존 파일 삭제
            if (oldFilePath != null) {
                deleteFile(oldFilePath);
            }
            String newFilePath = saveFile(file);
            post.setFilePath(newFilePath);
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        if (post.getFilePath() != null) {
            deleteFile(post.getFilePath());
        }
        postRepository.deleteById(id);
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

    private void deleteFile(String filePath) {
        try {
            Path path = Paths.get(uploadDir).resolve(filePath).normalize();
            Files.deleteIfExists(path);
            System.out.println("파일 삭제 완료: " + path.toString());
        } catch (IOException e) {
            System.err.println("파일 삭제 오류: " + filePath);
            e.printStackTrace();
        }
    }

}
