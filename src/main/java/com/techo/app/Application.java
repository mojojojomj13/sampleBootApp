package com.techo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.techo.app.services.SatisfactionService;

/**
 * 
 * @author Prithvish Mukherjee
 *
 *         Main class that loads the Spring container
 */
@SpringBootApplication
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { "com.techo.app" })
public class Application {
	public static void main(String[] args) throws Throwable {
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
		SatisfactionService service = (SatisfactionService) ctx.getBean("satisfactionService");
		Long res = service.getMaxSatisfaction();
		System.out.println("MAX VALUE >>>> :: " + res);
	}

}
