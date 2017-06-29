package Main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// The @SpringBootApplication annotation is equivalent to using
// @Configuration, @EnableAutoConfiguration and @ComponentScan with their default attributes
// Whole project start here. And requests are send to Controllers
@SpringBootApplication
public class JoJoFinder {
    public static void main(String[] args) {
        SpringApplication.run(JoJoFinder.class, args);
    }
}
