package com.matthenry87.restapi;

import com.matthenry87.restapi.ws.CatFactsRestTemplateConfig;
import com.matthenry87.restapi.ws.DilbertWebServiceClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Slf4j
@EnableMongoAuditing
@SpringBootApplication
@EnableConfigurationProperties({ CatFactsRestTemplateConfig.class, DilbertWebServiceClientConfig.class})
public class RestapiApplication {

	public static void main(String[] args) {

		SpringApplication.run(RestapiApplication.class, args);
	}

}
