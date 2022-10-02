package talkwith.semogong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Auditing기능을 사용하기 위한 설정
@SpringBootApplication
public class SemogongApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemogongApplication.class, args);
	}

}
