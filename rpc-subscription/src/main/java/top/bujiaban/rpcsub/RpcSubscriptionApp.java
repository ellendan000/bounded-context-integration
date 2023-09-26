package top.bujiaban.rpcsub;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringCloudApplication
public class RpcSubscriptionApp {

    public static void main(String[] args) {
        SpringApplication.run(RpcSubscriptionApp.class, args);
    }
}
