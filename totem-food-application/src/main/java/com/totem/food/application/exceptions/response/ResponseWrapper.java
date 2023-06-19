package com.totem.food.application.exceptions.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseWrapper<T> {

    private T error;

}
