package com.matthenry87.restapi.ws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "dilbert")
public class DilbertWebServiceClientConfig {

    private String uri;

    @Bean
    public Jaxb2Marshaller dilbertClientMarshaller() {

        var jaxb2Marshaller = new Jaxb2Marshaller();

        jaxb2Marshaller.setContextPath("com.matthenry87.restapi.dilbert");

        return jaxb2Marshaller;
    }

    @Bean
    public DilbertWebServiceClient dilbertClient(Jaxb2Marshaller dilbertClientMarshaller) {

        var dilbertWebServiceClient = new DilbertWebServiceClient();

        dilbertWebServiceClient.setDefaultUri(uri);
        dilbertWebServiceClient.setMarshaller(dilbertClientMarshaller);
        dilbertWebServiceClient.setUnmarshaller(dilbertClientMarshaller);

        return dilbertWebServiceClient;
    }
}
