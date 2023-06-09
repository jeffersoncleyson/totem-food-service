package com.totem.food.domain.exceptions;

import lombok.Getter;

@Getter
public class InvalidDomainField extends RuntimeException {

    private final String domainName;
    private final String field;
    private final String message;

    public InvalidDomainField(Class<?> domain, String field, String message){
        super(String.format("Domain [%s] Field [%s] Error [%s]", domain.getName(), field, message));
        this.domainName = domain.getName();
        this.field = field;
        this.message = message;
    }
}
