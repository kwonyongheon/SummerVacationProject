package SummerVacationProject.HCI.web.domain;

import lombok.Getter;

@Getter
public enum GalleryBoardType {
    freeBoard("자유"),
    humorBoard("유머"),
    questionBoard("질문"),
    videoBoard("영상"),
    lolTournamentBoard("롤대회");

    private final String viewName;

    GalleryBoardType(String viewName) {
        this.viewName = viewName;
    }
}
