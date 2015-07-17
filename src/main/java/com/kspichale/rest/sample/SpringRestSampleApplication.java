package com.kspichale.rest.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.models.dto.ApiInfo;
import com.mangofactory.swagger.plugin.EnableSwagger;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;

@ComponentScan
@EnableAutoConfiguration
@EnableSwagger
public class SpringRestSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestSampleApplication.class, args);
	}

	private SpringSwaggerConfig springSwaggerConfig;

	@Autowired
	public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
		this.springSwaggerConfig = springSwaggerConfig;
	}

	@Bean
	public SwaggerSpringMvcPlugin group1Plugin() {
		return new SwaggerSpringMvcPlugin(springSwaggerConfig)
				.includePatterns("/products.*?").apiInfo(apiInfo())
				.swaggerGroup("api");
	}

	private ApiInfo apiInfo() {
		ApiInfo apiInfo = new ApiInfo("The Example Product HTTP JSON API",
				"This is a description of your API.", "API TOS",
				"me@wherever.com", "API License", "API License URL");
		return apiInfo;
	}
}
