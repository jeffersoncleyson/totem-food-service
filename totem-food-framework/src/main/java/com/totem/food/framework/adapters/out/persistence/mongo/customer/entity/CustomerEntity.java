package com.totem.food.framework.adapters.out.persistence.mongo.customer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "customer")
public class CustomerEntity {

    @Id
    private String id;

    @NotBlank
    private String name;

    @CPF
    private String cpf;

    @Email
    private String email;

    private String mobile;

    private String password;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;

}
