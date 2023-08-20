package com.totem.food.framework.exceptions;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.domain.exceptions.InvalidEnum;
import com.totem.food.domain.exceptions.InvalidStatusException;
import com.totem.food.domain.exceptions.InvalidStatusTransition;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RestResponseEntityExceptionHandlerTest {

    private static final List<Class<? extends Throwable>> EXCEPTED_EXCEPTIONS = new ArrayList<>();

    static {
        EXCEPTED_EXCEPTIONS.add(ElementNotFoundException.class);
        EXCEPTED_EXCEPTIONS.add(ElementExistsException.class);
        EXCEPTED_EXCEPTIONS.add(InvalidStatusException.class);
        EXCEPTED_EXCEPTIONS.add(InvalidStatusTransition.class);
        EXCEPTED_EXCEPTIONS.add(ExternalCommunicationInvalid.class);
        EXCEPTED_EXCEPTIONS.add(InvalidEnum.class);
        EXCEPTED_EXCEPTIONS.add(InvalidInput.class);
    }

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @AfterEach
    void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    void checkAnnotationIsPresent(){
        assertTrue(RestResponseEntityExceptionHandler.class.isAnnotationPresent(ControllerAdvice.class));
    }

    @Test
    void checkAnnotationMethods(){
        final var actualExceptions = getThrowableClassesFromExceptionHandler();
        assertEquals(CollectionUtils.size(EXCEPTED_EXCEPTIONS), CollectionUtils.size(actualExceptions));
        for (Class<? extends Throwable> exceptedException : EXCEPTED_EXCEPTIONS) {
            assertTrue(actualExceptions.contains(exceptedException));
        }
    }

    private List<Class<? extends Throwable>> getThrowableClassesFromExceptionHandler(){
        return Arrays.stream(RestResponseEntityExceptionHandler.class.getDeclaredMethods())
                .map(method -> {
                    return Arrays.stream(method.getAnnotations())
                            .filter(a -> a.annotationType().equals(ExceptionHandler.class))
                            .map(ExceptionHandler.class::cast)
                            .map(ExceptionHandler::value)
                            .map(Arrays::asList)
                            .flatMap(List::stream)
                            .toList();
                })
                .flatMap(List::stream)
                .toList();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ElementNotFoundException",
            "ElementExistsException",
            "InvalidStatusException",
            "InvalidStatusTransition",
            "ExternalCommunicationInvalid",
            "InvalidEnum",
            "InvalidInput"
    })
    void elementNotFoundException(String exceptionType) throws Exception {

        final var httpServletRequest = get("/test/".concat(exceptionType));

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }


    @RestController
    public class TestController {

        @RequestMapping(value = "/test/{exceptionType}", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
        public void teste(@PathVariable String exceptionType){

            if (ElementNotFoundException.class.getSimpleName().equals(exceptionType)) {
                throw new ElementNotFoundException("");
            }

            if (ElementExistsException.class.getSimpleName().equals(exceptionType)) {
                throw new ElementExistsException("");
            }

            if (InvalidStatusException.class.getSimpleName().equals(exceptionType)) {
                throw new InvalidStatusException("domain", "init", "end");
            }

            if (InvalidStatusTransition.class.getSimpleName().equals(exceptionType)) {
                throw new InvalidStatusTransition("init", "end", Set.of("init", "finalized"));
            }

            if (ExternalCommunicationInvalid.class.getSimpleName().equals(exceptionType)) {
                throw new ExternalCommunicationInvalid("");
            }

            if (InvalidEnum.class.getSimpleName().equals(exceptionType)) {
                throw new InvalidEnum("test", Set.of("init", "end"));
            }

            if (InvalidInput.class.getSimpleName().equals(exceptionType)) {
                throw new InvalidInput("");
            }

        }
    }



}