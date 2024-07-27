package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {
    private String email;
    private String password;
    private String tagLine;
    private String gameName;
    private String birth;
}
