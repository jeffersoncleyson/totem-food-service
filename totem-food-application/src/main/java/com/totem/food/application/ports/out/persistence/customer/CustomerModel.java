package com.totem.food.application.ports.out.persistence.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerModel {

    private String id;
    private String name;
    private String cpf;
    private String email;
    private String mobile;
    private String password;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
