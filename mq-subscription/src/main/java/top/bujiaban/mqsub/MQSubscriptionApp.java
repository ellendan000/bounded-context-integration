package top.bujiaban.mqsub;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients
@EnableJpaAuditing
@EnableAsync
@SpringCloudApplication
public class MQSubscriptionApp {

    public static void main(String[] args) {
        SpringApplication.run(MQSubscriptionApp.class, args);
    }
}
