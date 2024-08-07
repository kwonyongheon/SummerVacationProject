//package SummerVacationProject.HCI.web.controller;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//public class ApiController {
//
//    @GetMapping("/api/check-authentication")
//    public Map<String, Boolean> checkAuthentication() {
//        Map<String, Boolean> response = new HashMap<>();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        response.put("authenticated", authentication != null && authentication.isAuthenticated() &&
//                !"anonymousUser".equals(authentication.getPrincipal()));
//        return response;
//    }
//}
//보류
