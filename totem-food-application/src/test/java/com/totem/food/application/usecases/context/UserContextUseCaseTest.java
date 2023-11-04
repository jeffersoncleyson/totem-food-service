package com.totem.food.application.usecases.context;

import com.totem.food.application.ports.in.dtos.context.XUserIdentifierContextDto;
import com.totem.food.application.usecases.commons.IContextUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserContextUseCaseTest {

    @Spy
    private IContextUseCase<XUserIdentifierContextDto, String> iContextUseCase;

    @BeforeEach
    public void setUp() {
        iContextUseCase = new UserContextUseCase();
    }

    @Test
    void testSetContext() {

        //## Given - When
        var dto = new XUserIdentifierContextDto("x-user-identifier");

        //## Then
        assertDoesNotThrow(() -> iContextUseCase.setContext(dto));

    }

    @Test
    void testGetContext() {

        //## Given
        var dto = new XUserIdentifierContextDto("x-user-identifier");
        iContextUseCase.setContext(dto);

        //## When
        String result = iContextUseCase.getContext();

        //## Then
        assertEquals(dto.getIdentifier(), result);
    }

    @Test
    void testclearContext() {

        //## Given
        var dto = new XUserIdentifierContextDto("x-user-identifier");
        iContextUseCase.setContext(dto);

        //## When - Then
        assertDoesNotThrow(() -> iContextUseCase.clearContext());
    }

}