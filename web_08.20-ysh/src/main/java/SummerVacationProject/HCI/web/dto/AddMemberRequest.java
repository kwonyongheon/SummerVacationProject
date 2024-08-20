package SummerVacationProject.HCI.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest { //용헌 코드
    private String email;
    private String password;
    private String nickName;
    private String tagLine;
    private String gameName;
    private String birth;
    private boolean admin;  //관리자 등록여부 ysh
}
