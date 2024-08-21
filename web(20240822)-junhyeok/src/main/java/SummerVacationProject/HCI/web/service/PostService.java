package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.Post;
import SummerVacationProject.HCI.web.domain.Recommendation;
import SummerVacationProject.HCI.web.dto.PostRequest;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import SummerVacationProject.HCI.web.repository.MemberRepository;
import SummerVacationProject.HCI.web.repository.PostRepository;
import SummerVacationProject.HCI.web.repository.RecommendationRepository;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final GalleryRepository galleryRepository;
    private final MemberRepository memberRepository;  // 이 부분을 추가합니다.
    private final RecommendationRepository recommendationRepository;

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
                .filePath(filePath)
                .viewCount(0)
                .recommendCount(0)
                .build();

        return postRepository.save(post);
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
    public void toggleRecommendation(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + postId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + memberId));

        Optional<Recommendation> existingRecommendation = recommendationRepository.findByMemberIdAndPostId(memberId, postId);

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
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id:" + id));

        if (post.getFilePath() != null) {
            deleteFile(post.getFilePath());
        }

        recommendationRepository.deleteByPostId(id);
        postRepository.deleteById(id);
    }

    @Transactional
    public void deleteUserPostsInGallery(Long galleryId, Long memberId) {
        List<Post> userPostsInGallery = postRepository.findByGalleryIdAndAuthorId(galleryId, memberId);

        for (Post post : userPostsInGallery) {
            if (post.getFilePath() != null) {
                deleteFile(post.getFilePath());
            }
            recommendationRepository.deleteByPostId(post.getId());
        }

        postRepository.deleteAll(userPostsInGallery);
    }

    @Transactional
    public void deleteAllPostsInGallery(Long galleryId) {
        List<Post> posts = postRepository.findByGalleryIdOrderByIdDesc(galleryId);

        for (Post post : posts) {
            if (post.getFilePath() != null) {
                deleteFile(post.getFilePath());
            }
            recommendationRepository.deleteByPostId(post.getId());
        }

        postRepository.deleteAll(posts);
    }

    @Transactional
    public void deleteUserPosts(Long memberId) {
        List<Post> userPosts = postRepository.findByAuthorId(memberId);

        for (Post post : userPosts) {
            if (post.getFilePath() != null) {
                deleteFile(post.getFilePath());
            }
            recommendationRepository.deleteByPostId(post.getId());
        }

        postRepository.deleteAll(userPosts);
    }

    @Transactional
    public void deleteAllPosts() {
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            if (post.getFilePath() != null) {
                deleteFile(post.getFilePath());
            }
            recommendationRepository.deleteByPostId(post.getId());
        }

        postRepository.deleteAll();
    }
}
