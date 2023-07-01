package com.totem.food.framework.adapters.out.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "mail.smtp")
public class EmailConfigs {


    @NotNull
    private String host;

    @NotNull
    private int port;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private Boolean auth;

    @NotNull
    private Boolean tls;

    @NotNull
    private String email;
}
