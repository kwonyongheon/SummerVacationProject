package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PlayerMatch {
    private String queueType;
    private String daysAgo;
    private String duration;
    private boolean win;
    private String championName;
    private String championIconUrl;
    private int kills;
    private int deaths;
    private int assists;
    private List<Participant> allies;  // Updated to use Participant
    private List<Participant> enemies;  // 이 부분을 추가
}
