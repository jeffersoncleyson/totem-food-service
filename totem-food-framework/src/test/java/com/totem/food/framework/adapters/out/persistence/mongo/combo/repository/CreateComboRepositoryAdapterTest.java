package com.totem.food.framework.adapters.out.persistence.mongo.combo.repository;

import com.totem.food.application.ports.out.persistence.combo.ComboModel;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.mapper.IComboEntityMapper;
import lombok.SneakyThrows;
import mocks.domains.ComboDomainMock;
import mocks.entity.ComboEntityMock;
import mocks.models.ComboModelMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateComboRepositoryAdapterTest {

    @Mock
    private CreateComboRepositoryAdapter.ComboRepositoryMongoDB repository;
    @Spy
    private IComboEntityMapper iComboEntityMapper = Mappers.getMapper(IComboEntityMapper.class);

    private CreateComboRepositoryAdapter comboRepositoryAdapter;
    private AutoCloseable closeable;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        comboRepositoryAdapter = new CreateComboRepositoryAdapter(repository, iComboEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void saveItem() {

        //### Given - Mocks
        var comboDomain = ComboModelMock.getMock();
        var comboEntity = ComboEntityMock.getMock();
        when(repository.save(Mockito.any(ComboEntity.class))).thenReturn(comboEntity);

        //### When
        final var comboDomainSaved = comboRepositoryAdapter.saveItem(comboDomain);

        //### Then
        verify(iComboEntityMapper, times(1)).toEntity(Mockito.any(ComboModel.class));
        verify(repository, times(1)).save(Mockito.any(ComboEntity.class));
        verify(iComboEntityMapper, times(1)).toModel(Mockito.any(ComboEntity.class));

        assertThat(comboDomain)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(comboDomainSaved);

        assertNotNull(comboDomainSaved.getId());
        assertEquals(comboEntity.getId(), comboDomainSaved.getId());

    }
}