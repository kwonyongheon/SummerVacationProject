package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Champion {
    private String id;
    private String name;
    private String title;
    private String imageUrl;
    private List<String> skins; // 스킨 목록
    private String lore; // 챔피언의 특징이나 설명
}
