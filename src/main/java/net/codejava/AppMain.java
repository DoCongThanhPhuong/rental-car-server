package net.codejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class AppMain {
    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
        System.out.println("http://localhost:8080/swagger-ui/index.html");
    }
}
