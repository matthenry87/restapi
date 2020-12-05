package com.matthenry87.restapi.ws;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DilbertWebServiceClientTest {

    @Autowired
    private DilbertWebServiceClient dilbertWebServiceClient;

    @Test
    void dailyDilbert() {

        var dailyDilbertResponse = dilbertWebServiceClient.dailyDilbert();

        assertThat(dailyDilbertResponse).isNotNull();
    }
}