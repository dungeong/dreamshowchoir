package kr.ulsan.dreamshowchoir.dungeong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DungeongApplication {

	public static void main(String[] args) {
		SpringApplication.run(DungeongApplication.class, args);
	}

}
