package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Participant {
    private String summonerName;
    private String championName;
    private String championIconUrl;
    private int kills;
    private int deaths;
    private int assists;
    private int champLevel;
}
