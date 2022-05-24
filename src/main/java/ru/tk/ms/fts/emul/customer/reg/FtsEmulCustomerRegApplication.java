package ru.tk.ms.fts.emul.customer.reg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FtsEmulCustomerRegApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtsEmulCustomerRegApplication.class, args);
    }

}
