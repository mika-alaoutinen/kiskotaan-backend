package com.mika.kiskotaan.validators;

import com.mika.kiskotaan.errors.badrequest.PlayerException;
import com.mika.kiskotaan.services.PlayerService;
import kiskotaan.openapi.model.NewPlayerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerResourceValidator {
    private final PlayerService service;

    public NewPlayerResource validateNameIsUnique(NewPlayerResource resource) {
        if (service.existsByName(resource.getName())) {
            throw new PlayerException(resource);
        }

        return resource;
    }
}
