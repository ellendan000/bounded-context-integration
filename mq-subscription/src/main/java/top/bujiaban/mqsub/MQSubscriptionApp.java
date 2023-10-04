package top.bujiaban.mqsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRetry
@EnableAsync
@EnableFeignClients(basePackages = {"top.bujiaban.common", "top.bujiaban.mqsub"})
@EnableJpaAuditing
@EntityScan(basePackages = {"top.bujiaban.common", "top.bujiaban.mqsub"})
@EnableJpaRepositories(basePackages = {"top.bujiaban.common", "top.bujiaban.mqsub"})
@SpringBootApplication(scanBasePackages = {"top.bujiaban.common", "top.bujiaban.mqsub"})
public class MQSubscriptionApp {

    public static void main(String[] args) {
        SpringApplication.run(MQSubscriptionApp.class, args);
    }
}
