package com.totem.food.application.exceptions.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseError {

    private String title;
    private String description;
}
