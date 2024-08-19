package SummerVacationProject.HCI.web.Exception;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 403 상태 코드를 URL 파라미터로 전달 ysh 수정
        int statusCode = HttpServletResponse.SC_FORBIDDEN;
        String redirectUrl = request.getContextPath() + "/access-denied?status=" + statusCode;
        response.sendRedirect(redirectUrl);

        
//        response.sendRedirect(request.getContextPath() + "/access-denied");
//        // 403 상태 코드를 URL 파라미터로 전달
//        int statusCode = HttpServletResponse.SC_FORBIDDEN;
//        String redirectUrl = request.getContextPath() + "/access-denied?status=" + statusCode;
//        response.sendRedirect(redirectUrl);


    }
}
