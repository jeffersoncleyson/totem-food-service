package com.totem.food;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "com.totem.food")
@EnableMongoRepositories(basePackages = "com.totem.food", considerNestedRepositories = true)
public class TotemFoodBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(TotemFoodBackendApplication.class,
			args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> getBeansFromContext(ctx);
	}

	private void getBeansFromContext(ApplicationContext ctx) {

		System.out.println("\nLet's inspect the beans provided by Spring Boot:\n");
		String[] beanNames = ctx.getBeanDefinitionNames();

		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			var bean = ctx.getType(beanName);
			if (bean != null) {
				var canonicalName = bean.getCanonicalName();
				if (canonicalName != null && canonicalName.startsWith("com.totem.food")) {
					System.out.println(beanName + "  -   " + canonicalName);
				}
			}
		}

		System.out.println("\n");
	}
}
