package com.mika.kiskotaan.controllers;

import kiskotaan.openapi.api.PlayersApi;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class PlayersController implements PlayersApi {

    @Override
    public ResponseEntity<PlayerResource> addPlayer(@Valid NewPlayerResource newPlayerResource) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletePlayer(@Size(min = 1) BigDecimal id) {
        return null;
    }

    @Override
    public ResponseEntity<PlayerResource> getPlayer(@Size(min = 1) BigDecimal id) {
        return null;
    }

    @Override
    public ResponseEntity<List<PlayerResource>> getPlayers() {
        return null;
    }
}
