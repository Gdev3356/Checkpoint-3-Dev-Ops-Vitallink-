package br.com.fiap.vitallink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VitallinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(VitallinkApplication.class, args);
	}
}
