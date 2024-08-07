package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.GalleryBoardType;
import SummerVacationProject.HCI.web.domain.Post;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GalleryController {

    @GetMapping("/gallery/{boardType}")
    public String getGalleryBoard(@PathVariable("boardType") String boardTypeName, Model model) {
        try {
            GalleryBoardType boardType = GalleryBoardType.valueOf(boardTypeName);
            model.addAttribute("boardType", boardType);
            model.addAttribute("posts", getPostsForBoard(boardType));
            model.addAttribute("galleryBoardTypes", GalleryBoardType.values()); //갤러리 항목에 GalleryBoardType enum 데이터 값
            return boardType.toString(); // 예를 들어, boardType.toString()이 "freeBoard"라면 freeBoard.html로 이동
        } catch (IllegalArgumentException e) {
            return "error"; // 적절한 에러 페이지로 설정
        }
    }

    private List<Post> getPostsForBoard(GalleryBoardType boardType) {
        // 실제 게시글 조회 로직을 구현합니다.
        // 데이터베이스에서 게시글을 가져오는 등의 작업이 필요합니다.
        return List.of(); // 임시로 빈 리스트를 반환
    }
}
