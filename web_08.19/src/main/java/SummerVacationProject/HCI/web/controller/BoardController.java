package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Board;
import SummerVacationProject.HCI.web.service.BoardService;
import SummerVacationProject.HCI.web.service.GalleryService;
import SummerVacationProject.HCI.web.service.MinorGalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/boards")
public class BoardController {  //보류

//    private final BoardService boardService;
//    private final GalleryService galleryService;  // assuming a GalleryService exists
//    private final MinorGalleryService minorGalleryService;  // assuming a MinorGalleryService exists
//
//    @Autowired
//    public BoardController(BoardService boardService, GalleryService galleryService, MinorGalleryService minorGalleryService) {
//        this.boardService = boardService;
//        this.galleryService = galleryService;
//        this.minorGalleryService = minorGalleryService;
//    }
//
//    @GetMapping
//    public List<Board> getAllBoards() {
//        return boardService.getAllBoards();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
//        Optional<Board> board = boardService.getBoardById(id);
//        return board.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

//    @PostMapping
//    public ResponseEntity<Board> createBoard(@RequestBody BoardRequestDto boardRequest) {
//        Gallery gallery = (Gallery) galleryService.findById(boardRequest.getGalleryId()).orElse(null);
//        MinorGallery minorGallery = minorGalleryService.findById(boardRequest.getMinorGalleryId()).orElse(null);
//        Member member = memberService.findById(boardRequest.getMemberId()).orElse(null);  // assuming a MemberService exists
//
//        Board board = boardService.createBoard(
//                boardRequest.getTitle(),
//                boardRequest.getDescription(),
//                gallery,
//                minorGallery,
//                member
//        );
//
//        return ResponseEntity.ok(board);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board updatedBoard) {
//        Board board = boardService.updateBoard(id, updatedBoard);
//        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
//        boardService.deleteBoard(id);
//        return ResponseEntity.noContent().build();
//    }
}
