package uz.hamkorbank.appwebhooktelegrammbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.beans.BeanProperty;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class AppWebhookTelegrammBotApplication {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(AppWebhookTelegrammBotApplication.class, args);
    }

}
