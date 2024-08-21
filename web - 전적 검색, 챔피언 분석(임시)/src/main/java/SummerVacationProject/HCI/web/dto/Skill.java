package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Skill { // 기존 Spell 클래스를 Skill로 변경
    private String name;
    private String description;
    private String imageUrl;
}