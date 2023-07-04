package com.lllebin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@ServletComponentScan
@SpringBootApplication
public class EasyeatFoodDeliveryApplication {
	public static void main(String[] args) {
		log.info("项目启动成功...");
		SpringApplication.run(EasyeatFoodDeliveryApplication.class, args);
	}

}
