package com.totem.food.domain.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFound extends RuntimeException {

    private final String resource;
    private final String message;

    public ResourceNotFound(Class<?> resource, String message) {
        super(String.format("Resource [%s] Message: [%s]", resource.getName(), message));
        this.resource = resource.getName();
        this.message = message;
    }

}
