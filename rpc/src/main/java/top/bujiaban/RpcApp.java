package top.bujiaban;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringCloudApplication
public class RpcApp {

    public static void main(String[] args) {
        SpringApplication.run(RpcApp.class, args);
    }
}
