package top.bujiaban.rpcsub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = {"top.bujiaban.common", "top.bujiaban.rpcsub"})
@EnableJpaAuditing
@EntityScan(basePackages = {"top.bujiaban.common", "top.bujiaban.rpcsub"})
@EnableJpaRepositories(basePackages = {"top.bujiaban.common", "top.bujiaban.rpcsub"})
@SpringBootApplication(scanBasePackages = {"top.bujiaban.common", "top.bujiaban.rpcsub"})
public class RpcSubscriptionApp {

    public static void main(String[] args) {
        SpringApplication.run(RpcSubscriptionApp.class, args);
    }
}
