package top.bujiaban;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class RedissonApp {

    public static void main(String[] args) {
        SpringApplication.run(RedissonApp.class, args);
    }
}
