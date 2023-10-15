package com.totem.food.framework.config;

import com.totem.food.framework.adapters.in.rest.interceptors.XUserIdentifierInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_ORDER;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final XUserIdentifierInterceptor xUserIdentifierInterceptor;

    public static final List<String> EXCLUDE_PATH_PATTERNS = List.of(
            "/v2/api-docs/**",
            "/v2/api-docs**",
            "/v3/api-docs/**",
            "/v3/api-docs**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/favicon.ico",
            "/error",
            "/webjars/**"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(xUserIdentifierInterceptor)
                .addPathPatterns(API_VERSION_1 + TOTEM_ORDER + "/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }
}
