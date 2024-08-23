package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.dto.GalleryListViewResponse;
import SummerVacationProject.HCI.web.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MinorGalleryViewController {
    private final GalleryService galleryService;

    @GetMapping("/m-create")
    public String createMinorGallery(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<GalleryListViewResponse> Galleries = galleryService.findall().stream()
                .map(GalleryListViewResponse::new)
                .toList();
        model.addAttribute("Galleries", Galleries);
        model.addAttribute("nickname", userDetails.getUsername());
        return "create-minorGallery";
    }

}
