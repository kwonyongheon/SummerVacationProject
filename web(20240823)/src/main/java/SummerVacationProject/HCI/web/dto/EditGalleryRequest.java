package SummerVacationProject.HCI.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EditGalleryRequest {
    private String name;
    private String description;
}
