package SummerVacationProject.HCI.web.service;

import SummerVacationProject.HCI.web.domain.Board;
import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.domain.MinorGallery;
import SummerVacationProject.HCI.web.repository.BoardRepository;
import SummerVacationProject.HCI.web.repository.GalleryRepository;
import SummerVacationProject.HCI.web.repository.MinorGalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService { //보류

    private final BoardRepository boardRepository;
    private final GalleryRepository galleryRepository;
    private final MinorGalleryRepository minorGalleryRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository,
                        GalleryRepository galleryRepository,
                        MinorGalleryRepository minorGalleryRepository) {
        this.boardRepository = boardRepository;
        this.galleryRepository = galleryRepository;
        this.minorGalleryRepository = minorGalleryRepository;
    }

    //모든 게시판 조회
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    //게시판 조회
    public Optional<Board> getBoardById(Long id) {
        return boardRepository.findById(id);
    }

    //게시판 저장
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    //게시판 삭제
    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    //게시판 수정
    public Board updateBoard(Long id, Board updatedBoard) {
        if (boardRepository.existsById(id)) {
            updatedBoard.setId(id);
            return boardRepository.save(updatedBoard);
        }
        return null; // or throw exception
    }

    public Board createBoard(String title, String description, Gallery gallery, MinorGallery minorGallery, Member member) {
        Board board = Board.builder()
                .title(title)
                .description(description)
                .gallery(gallery)
                .minorGallery(minorGallery)
                .member(member)
                .build();

        return boardRepository.save(board);
    }
}