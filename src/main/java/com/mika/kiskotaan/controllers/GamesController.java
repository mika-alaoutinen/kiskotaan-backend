package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.services.GameService;
import kiskotaan.openapi.api.GamesApi;
import kiskotaan.openapi.model.GameResource;
import kiskotaan.openapi.model.NewGameResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class GamesController implements GamesApi {
    private final GameService service;

    @Override
    public ResponseEntity<Void> endGame(BigDecimal id) {
        service.endGame(id.longValue());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<GameResource> getGame(BigDecimal id) {
        return ResponseEntity.ok(service.getGame(id.longValue()));
    }

    @Override
    public ResponseEntity<GameResource> startGame(@Valid NewGameResource newGameResource) {
        return new ResponseEntity<>(service.startGame(newGameResource), HttpStatus.CREATED);
    }
}
