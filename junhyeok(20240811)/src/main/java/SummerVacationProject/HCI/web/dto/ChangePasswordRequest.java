package SummerVacationProject.HCI.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
