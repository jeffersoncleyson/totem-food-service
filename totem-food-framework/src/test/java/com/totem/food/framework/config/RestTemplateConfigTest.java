package com.totem.food.framework.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RestTemplateConfigTest {

    @Test
    void checkAnnotationIsPresent(){
        assertTrue(RestTemplateConfig.class.isAnnotationPresent(Configuration.class));
    }

    @Test
    void checkInstanceOf(){
        final var restTemplateConfig = new RestTemplateConfig();
        final var restTemplate = restTemplateConfig.restTemplate(new RestTemplateBuilder());
        assertTrue(Objects.nonNull(restTemplate));
        assertTrue(RestTemplate.class.isInstance(restTemplate) );
    }
}