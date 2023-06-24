package com.totem.food.framework.adapters.in.rest.combo;

import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;
import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.ADM_COMBO;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;

@RestController
@RequestMapping(value = API_VERSION_1 + ADM_COMBO)
@AllArgsConstructor
public class AdministrativeComboRestApiAdapter implements ICreateRestApiPort<ComboCreateDto, ResponseEntity<ComboDto>> {

    private final ICreateUseCase<ComboCreateDto, ComboDto> iCreateUseCase;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ComboDto> create(@RequestBody ComboCreateDto item) {
        final var createdCombo = iCreateUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCombo);
    }
}
