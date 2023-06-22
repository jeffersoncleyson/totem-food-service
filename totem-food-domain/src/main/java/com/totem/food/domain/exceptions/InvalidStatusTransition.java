package com.totem.food.domain.exceptions;

import lombok.Getter;

import java.util.Set;

@Getter
public class InvalidStatusTransition extends RuntimeException {

    public InvalidStatusTransition(String source, String target, Set<String> allowedStatus){
        super(String.format("Transition from [%s] to [%s] failed, allowed status [%s]", source, target, allowedStatus));
    }
}
