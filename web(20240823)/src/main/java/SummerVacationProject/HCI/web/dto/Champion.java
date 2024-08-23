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
    private String lore; // 챔피언 설명
    private String imageUrl; // 이미지 url
    private List<String> skins; // 스킨
    private List<String> skinNames; // 스킨 이름을 저장하는 필드
    private List<Skill> skills; // 스킬
    private Stats stats; // 능력치 정보를 저장하는 필드
}