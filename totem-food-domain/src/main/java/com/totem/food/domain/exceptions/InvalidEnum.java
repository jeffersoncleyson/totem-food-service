package com.totem.food.domain.exceptions;

import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidEnum extends RuntimeException {

    public InvalidEnum(String value, Set<String> allowedStatus){
        super(String.format("Invalid value [%s], allowed status [%s]", value, allowedStatus));
    }
}
