package top.bujiaban.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients(basePackages = {"top.bujiaban.common", "top.bujiaban.rpc"})
@EnableJpaAuditing
@EntityScan(basePackages = {"top.bujiaban.common", "top.bujiaban.rpc"})
@EnableJpaRepositories(basePackages = {"top.bujiaban.common", "top.bujiaban.rpc"})
@SpringBootApplication(scanBasePackages = {"top.bujiaban.common", "top.bujiaban.rpc"})
public class RpcApp {

    public static void main(String[] args) {
        SpringApplication.run(RpcApp.class, args);
    }
}
