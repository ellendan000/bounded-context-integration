package top.bujiaban.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringCloudApplication
public class RpcApp {

    public static void main(String[] args) {
        SpringApplication.run(RpcApp.class, args);
    }
}