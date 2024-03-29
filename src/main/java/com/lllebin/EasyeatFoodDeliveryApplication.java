package com.lllebin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableCaching
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
public class EasyeatFoodDeliveryApplication {
	public static void main(String[] args) {
		log.info("项目启动成功...");
		SpringApplication.run(EasyeatFoodDeliveryApplication.class, args);
	}

}
