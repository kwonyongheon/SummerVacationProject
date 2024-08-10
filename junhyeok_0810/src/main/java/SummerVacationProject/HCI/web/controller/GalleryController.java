package SummerVacationProject.HCI.web.controller;

import SummerVacationProject.HCI.web.domain.Gallery;
import SummerVacationProject.HCI.web.domain.Member;
import SummerVacationProject.HCI.web.dto.GalleryRequest;
import SummerVacationProject.HCI.web.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member-galleries")
public class GalleryController {

    private final GalleryService galleryService;

    @GetMapping
    public String getAllGalleries(Model model, Principal principal) {
        if (principal != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Member member = (Member) authentication.getPrincipal();
            List<Gallery> myGalleries = galleryService.getGalleriesByMember(member);
            List<Gallery> otherGalleries = galleryService.getOtherGalleries(member);
            model.addAttribute("myGalleries", myGalleries);
            model.addAttribute("otherGalleries", otherGalleries);
        }
        return "member-galleries";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createGallery(@ModelAttribute GalleryRequest galleryRequest, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        try {
            galleryService.createGallery(member, galleryRequest);
            return "갤러리를 생성하였습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public String deleteGallery(@PathVariable Long id, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        try {
            galleryService.deleteGallery(member, id);
            return "갤러리를 삭제하였습니다.";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

}
