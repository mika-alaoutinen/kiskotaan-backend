package com.mika.kiskotaan.controllers;

import kiskotaan.openapi.api.GamesApi;
import kiskotaan.openapi.model.GameResource;
import kiskotaan.openapi.model.ScoreCardIdResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
public class GamesController implements GamesApi {

    @Override
    public ResponseEntity<GameResource> endGame(BigDecimal id) {
        return null;
    }

    @Override
    public ResponseEntity<GameResource> startGame(@Valid ScoreCardIdResource scoreCardIdResource) {
        return null;
    }
}
