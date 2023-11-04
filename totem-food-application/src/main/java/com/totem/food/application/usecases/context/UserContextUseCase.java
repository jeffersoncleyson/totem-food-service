package com.totem.food.application.usecases.context;

import com.totem.food.application.ports.in.dtos.context.XUserIdentifierContextDto;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IContextUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class UserContextUseCase implements IContextUseCase<XUserIdentifierContextDto, String> {

    private final ThreadLocal<String> context = new InheritableThreadLocal<>();

    @Override
    public void setContext(XUserIdentifierContextDto value) {
        context.set(value.getIdentifier());
    }

    @Override
    public String getContext() {
        return context.get();
    }

    @Override
    public void clearContext() {
        context.remove();
    }
}
