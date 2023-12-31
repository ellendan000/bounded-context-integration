package top.bujiaban.seataat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = {"top.bujiaban.common", "top.bujiaban.seataat"})
@EnableJpaAuditing
@EntityScan(basePackages = {"top.bujiaban.common", "top.bujiaban.seataat"})
@EnableJpaRepositories(basePackages = {"top.bujiaban.common", "top.bujiaban.seataat"})
@SpringBootApplication(scanBasePackages = {"top.bujiaban.common", "top.bujiaban.seataat"})
public class SeataATApp {
    public static void main(String[] args) {
        SpringApplication.run(SeataATApp.class, args);
    }
}
