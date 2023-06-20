package com.totem.food.application.ports.in.dtos.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerCreateDto {

    private String name;
    private String email;
    private String cpf;
    private String mobile;
    private String password;
}
