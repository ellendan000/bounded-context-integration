package top.bujiaban.seatatcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = {"top.bujiaban.common", "top.bujiaban.seatatcc"})
@EnableJpaAuditing
@EntityScan(basePackages = {"top.bujiaban.common", "top.bujiaban.seatatcc"})
@EnableJpaRepositories(basePackages = {"top.bujiaban.common", "top.bujiaban.seatatcc"})
@SpringBootApplication(scanBasePackages = {"top.bujiaban.common", "top.bujiaban.seatatcc"})
public class SeataTccApp {
    public static void main(String[] args) {
        SpringApplication.run(SeataTccApp.class, args);
    }
}
