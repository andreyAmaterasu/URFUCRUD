package ru.kostromin.urfu;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class UrfuApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrfuApplication.class, args);
	}

}
