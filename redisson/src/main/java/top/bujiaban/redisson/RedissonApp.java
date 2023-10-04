package top.bujiaban.redisson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = {"top.bujiaban.common", "top.bujiaban.redisson"})
@EnableJpaAuditing
@EntityScan(basePackages = {"top.bujiaban.common", "top.bujiaban.redisson"})
@EnableJpaRepositories(basePackages = {"top.bujiaban.common", "top.bujiaban.redisson"})
@SpringBootApplication(scanBasePackages = {"top.bujiaban.common", "top.bujiaban.redisson"})
public class RedissonApp {

    public static void main(String[] args) {
        SpringApplication.run(RedissonApp.class, args);
    }
}
