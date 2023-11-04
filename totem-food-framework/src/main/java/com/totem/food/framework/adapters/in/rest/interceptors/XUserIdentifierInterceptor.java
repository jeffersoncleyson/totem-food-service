package com.totem.food.framework.adapters.in.rest.interceptors;

import com.totem.food.application.exceptions.HeaderMissingException;
import com.totem.food.application.ports.in.dtos.context.XUserIdentifierContextDto;
import com.totem.food.application.usecases.commons.IContextUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class XUserIdentifierInterceptor implements HandlerInterceptor {

    public static final String HEADER_X_ANONYMOUS_USER_IDENTIFIER = "x-user-identifier";
    private final IContextUseCase<XUserIdentifierContextDto, String> iContextUseCase;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        var xUserIdentifierContextDto = validateHeaderXUserIdentifier(request);
        iContextUseCase.setContext(xUserIdentifierContextDto);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        iContextUseCase.clearContext();
    }

    private XUserIdentifierContextDto validateHeaderXUserIdentifier(HttpServletRequest request) {

        var xUserIdentifier = request.getHeader(HEADER_X_ANONYMOUS_USER_IDENTIFIER);

        return Optional.ofNullable(xUserIdentifier)
                .filter(StringUtils::isNotBlank)
                .map(XUserIdentifierContextDto::new)
                .orElseThrow(() -> new HeaderMissingException("Missing header ".concat(HEADER_X_ANONYMOUS_USER_IDENTIFIER)));
    }
}
