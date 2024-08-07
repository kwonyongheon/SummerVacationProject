package SummerVacationProject.HCI.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateMemberRequest {
    private String nickName;
    private String gameName;
    private String tagLine;
    private String birth;
}
