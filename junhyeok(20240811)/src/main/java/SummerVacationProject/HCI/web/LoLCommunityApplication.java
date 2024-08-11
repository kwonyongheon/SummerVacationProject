package SummerVacationProject.HCI.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LoLCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoLCommunityApplication.class, args);
	}

}
