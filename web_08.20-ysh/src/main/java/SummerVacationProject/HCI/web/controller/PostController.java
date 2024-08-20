package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.GalleryBoardType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PostController {
    @GetMapping("/postCreate")
    public String showPostCreateForm(Model model) {
        model.addAttribute("galleryBoardTypes", GalleryBoardType.values()); // 갤러리 항목 추가
        return "postCreate";
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("upload") MultipartFile file) {
        String imageUrl = ""; // 이미지가 저장된 후 접근할 수 있는 URL
        // 파일을 서버에 저장하는 로직을 구현하고, 저장된 파일의 URL을 생성합니다.

        // 예제: 이미지 파일 저장 후 URL 생성
        try {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get("images/" + fileName);
            Files.write(path, file.getBytes());
            imageUrl = "/images/" + fileName; // 저장된 이미지에 접근할 수 있는 URL 설정
        } catch (IOException e) {
            e.printStackTrace();
        }

        // CKEditor에서 요구하는 JSON 형태로 응답
        Map<String, String> response = new HashMap<>();
        response.put("url", imageUrl);  // 이미지 URL을 반환

        return ResponseEntity.ok(response);
    }
}
