package com.totem.food.application.usecases.commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUniqueUseCaseTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private SearchUniqueUseCase searchUniqueUseCase;

    private Item item;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
    }

    @Test
    @DisplayName("Should return the item when the id is valid")
    void itemWhenIdIsValid() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Item result = searchUniqueUseCase.item(1L);

        assertNotNull(result);
        assertEquals(item, result);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw an exception when the id is invalid")
    void itemWhenIdIsInvalidThenThrowException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> {
            searchUniqueUseCase.item(0L);
        });

        verify(itemRepository, times(1)).findById(0L);
    }

}