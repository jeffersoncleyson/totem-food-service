package com.totem.food.framework.config;

import com.totem.food.application.usecases.annotations.UseCase;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCaseConfigTest {

    @Test
    void checkAnnotationIsPresent(){
        assertTrue(UseCaseConfig.class.isAnnotationPresent(Configuration.class));
        assertTrue(UseCaseConfig.class.isAnnotationPresent(ComponentScan.class));
        ComponentScan annotation = UseCaseConfig.class.getAnnotation(ComponentScan.class);
        assertEquals(1, annotation.basePackages().length);
        assertEquals("com.totem.food.application", annotation.basePackages()[0]);
        assertTrue(CollectionUtils.isNotEmpty(Arrays.asList(annotation.includeFilters())));
        assertEquals(1, annotation.includeFilters().length);
        assertTrue(CollectionUtils.isNotEmpty(Arrays.asList(annotation.includeFilters()[0].value())));
        assertEquals(1, annotation.includeFilters()[0].value().length);
        assertEquals(UseCase.class, annotation.includeFilters()[0].value()[0]);
    }

}