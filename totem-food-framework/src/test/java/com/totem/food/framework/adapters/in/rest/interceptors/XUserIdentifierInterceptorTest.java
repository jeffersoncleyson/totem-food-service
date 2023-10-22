package com.totem.food.framework.adapters.in.rest.interceptors;

import com.totem.food.application.exceptions.HeaderMissingException;
import com.totem.food.application.ports.in.dtos.context.XUserIdentifierContextDto;
import com.totem.food.application.usecases.commons.IContextUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import static com.totem.food.framework.adapters.in.rest.interceptors.XUserIdentifierInterceptor.HEADER_X_ANONYMOUS_USER_IDENTIFIER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class XUserIdentifierInterceptorTest {

    @Mock
    private IContextUseCase<XUserIdentifierContextDto, String> iContextUseCase;

    @Spy
    private HandlerInterceptor handlerInterceptor;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        handlerInterceptor = new XUserIdentifierInterceptor(iContextUseCase);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    void testPreHandle() throws Exception {

        //## Given
        String identifier = "x-user-identifier";
        request.addHeader(HEADER_X_ANONYMOUS_USER_IDENTIFIER, identifier);

        //## When
        var result = handlerInterceptor.preHandle(request, response, new Object());

        //## Then
        assertTrue(result);
        verify(iContextUseCase, times(1)).setContext(any());
    }


    @Test
    void testPreHandleWhenHeaderMissingThenThrowHeaderMissingException() {

        // Given - When
        var exception = assertThrows(HeaderMissingException.class,
                () -> handlerInterceptor.preHandle(request, response, new Object()));

        //## Then
        assertEquals("Missing header ".concat(HEADER_X_ANONYMOUS_USER_IDENTIFIER), exception.getMessage());
        verify(iContextUseCase, never()).setContext(any(XUserIdentifierContextDto.class));
    }

    @Test
    void testPreHandleWhenHeaderPresentButBlankThenThrowHeaderMissingException() {

        //## Given
        request.addHeader(HEADER_X_ANONYMOUS_USER_IDENTIFIER, "");

        //## When
        var exception = assertThrows(HeaderMissingException.class,
                () -> handlerInterceptor.preHandle(request, response, new Object()));

        //## Then
        assertEquals("Missing header ".concat(HEADER_X_ANONYMOUS_USER_IDENTIFIER), exception.getMessage());
    }

    @Test
    void testPostHandleWhenIContextUseCaseCalledThenClearContextMethodCalled() throws Exception {

        //## Given - When
        handlerInterceptor.postHandle(request, response, new Object(), null);

        //## Then
        verify(iContextUseCase).clearContext();
    }

}