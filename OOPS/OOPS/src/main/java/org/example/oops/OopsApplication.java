package org.example.oops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OopsApplication.class, args);
		System.out.println("API is running at: http://localhost:8080/api/menus");
	}

}
