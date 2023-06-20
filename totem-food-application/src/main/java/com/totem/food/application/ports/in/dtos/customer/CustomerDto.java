package com.totem.food.application.ports.in.dtos.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private String id;
    private String name;
    private String cpf;
    private String email;
    private String mobile;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
