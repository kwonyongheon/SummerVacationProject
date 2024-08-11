package SummerVacationProject.HCI.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.img-path}")
    private String imgPath;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 기본 경로로 리디렉션 설정
        registry.addRedirectViewController("/", "/index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(imgPath);
    }
}
