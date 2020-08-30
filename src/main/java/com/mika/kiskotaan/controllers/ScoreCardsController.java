package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.services.ScoreCardService;
import kiskotaan.openapi.api.ScoreCardsApi;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import kiskotaan.openapi.model.ScoreRowResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class ScoreCardsController implements ScoreCardsApi {
    private final ScoreCardService service;

    @Override
    public ResponseEntity<ScoreCardResource> addScoreCard(@Valid NewScoreCardResource newScoreCardResource) {
        return new ResponseEntity<>(service.addScoreCard(newScoreCardResource), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteScoreCard(BigDecimal id) {
        service.deleteScoreCard(id.longValue());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<ScoreCardResource> getScoreCard(BigDecimal id) {
        return ResponseEntity.ok(service.getScoreCard(id.longValue()));
    }

    @Override
    public ResponseEntity<ScoreRowResource> updateScores(BigDecimal id, @Valid ScoreRowResource scoreRowResource) {
        return null;
    }
}
