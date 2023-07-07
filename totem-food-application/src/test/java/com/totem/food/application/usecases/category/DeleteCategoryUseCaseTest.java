package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.IDeleteRepositoryPort;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseTest {

    @Spy
    private ICategoryMapper iCategoryMapper = Mappers.getMapper(ICategoryMapper.class);

    @Mock
    private IDeleteRepositoryPort<String, CategoryModel> iDeleteRepositoryPort;

    private IDeleteUseCase<String, CategoryDto> iDeleteUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.iDeleteUseCase = new DeleteCategoryUseCase(iDeleteRepositoryPort);
    }

    @Test
    void removeItem() {
        iDeleteUseCase.removeItem(anyString());
        verify(iDeleteRepositoryPort).removeItem(anyString());
    }

}
