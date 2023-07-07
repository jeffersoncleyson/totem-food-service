package com.totem.food.application.ports.out.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationDto {

    private String email;
    private String subject;
    private String message;
}
