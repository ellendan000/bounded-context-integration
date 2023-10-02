package top.bujiaban.seataat;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableFeignClients
@EnableJpaAuditing
@SpringCloudApplication
public class SeataATApp {
    public static void main(String[] args) {
        SpringApplication.run(SeataATApp.class, args);
    }
}
