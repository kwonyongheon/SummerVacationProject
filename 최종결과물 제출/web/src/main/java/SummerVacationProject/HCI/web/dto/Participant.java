package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Participant {
    private String summonerName;
    private String championName;
    private int kills;
    private int deaths;
    private int assists;
    private int champLevel;
    private String championIconUrl;
    private String puuid;

    // 추가 필드: 스펠과 아이템
    private String spell1Id;
    private String spell2Id;
    private String item0;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    private String item6;

}

