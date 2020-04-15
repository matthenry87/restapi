package com.matthenry87.restapi.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @Mock
    private Pojo mock;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void init() {

        MockitoAnnotations.initMocks(this);

        TestController testController = new TestController(mock);

        mockMvc = MockMvcBuilders.standaloneSetup(testController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void methodArgumentNotValidException() throws Exception {
        // Arrange
        String json = objectMapper.writeValueAsString(new Pojo());

        // Act/Assert
        mockMvc.perform(post("/test")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void constraintViolationException() throws Exception {
        // Arrange/Act/Assert
        mockMvc.perform(put("/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void alreadyExistsException() throws Exception {
        // Arrange
        String json = createJson();

        when(mock.foo()).thenThrow(new AlreadyExistsException());

        // Act/Assert
        mockMvc.perform(post("/test")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void notFoundException() throws Exception {
        // Arrange
        String json = createJson();

        when(mock.foo()).thenThrow(new NotFoundException());

        // Act/Assert
        mockMvc.perform(post("/test")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void httpMessageNotReadableException() throws Exception {
        // Arrange
        Pojo pojo = new Pojo();
        pojo.setFoo("foo");
        pojo.setStatus(Status.OPEN);

        String json = objectMapper.writeValueAsString(pojo).replace("OPEN", "FOO");

        // Act/Assert
        mockMvc.perform(post("/test")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void httpMessageNotReadableException2() throws Exception {
        // Arrange/Act/Assert
        mockMvc.perform(post("/test")
                .content("}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void httpRequestMethodNotSupportedException() throws Exception {
        // Arrange/Act/Assert
        mockMvc.perform(delete("/test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void exception() throws Exception {
        // Arrange
        String json = createJson();

        when(mock.foo()).thenThrow(new RuntimeException());

        // Act/Assert
        mockMvc.perform(post("/test")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    private String createJson() throws JsonProcessingException {

        Pojo pojo = new Pojo();
        pojo.setFoo("foo");

        return objectMapper.writeValueAsString(pojo);
    }

}
