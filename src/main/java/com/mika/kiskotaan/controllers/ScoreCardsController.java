package com.mika.kiskotaan.controllers;

import kiskotaan.openapi.api.ScoreCardsApi;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import kiskotaan.openapi.model.ScoreRowResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController
public class ScoreCardsController implements ScoreCardsApi {

    @Override
    public ResponseEntity<ScoreCardResource> addScoreCard(@Valid NewScoreCardResource newScoreCardResource) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteScoreCard(@Size(min = 1) String id) {
        return null;
    }

    @Override
    public ResponseEntity<ScoreCardResource> getScoreCard(@Size(min = 1) String id) {
        return null;
    }

    @Override
    public ResponseEntity<ScoreRowResource> updateScores(@Valid ScoreRowResource scoreRowResource) {
        return null;
    }
}
