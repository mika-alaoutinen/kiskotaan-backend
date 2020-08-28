package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.services.PlayerService;
import kiskotaan.openapi.api.PlayersApi;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlayersController implements PlayersApi {
    private final PlayerService service;

    @Override
    public ResponseEntity<PlayerResource> addPlayer(@Valid NewPlayerResource newPlayerResource) {
        return new ResponseEntity<>(service.addPlayer(newPlayerResource), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deletePlayer(BigDecimal id) {
        service.deletePlayer(id.longValue());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<PlayerResource> getPlayer(BigDecimal id) {
        return ResponseEntity.ok(service.getPlayer(id.longValue()));
    }

    @Override
    public ResponseEntity<List<PlayerResource>> getPlayers() {
        return ResponseEntity.ok(service.getPlayers());
    }
}
