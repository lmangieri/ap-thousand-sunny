package leandromangieri.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"leandromangieri.portfolio"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
