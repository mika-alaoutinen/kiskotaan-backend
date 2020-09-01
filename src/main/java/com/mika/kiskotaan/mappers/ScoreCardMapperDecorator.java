package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.*;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ScoreCardMapperDecorator implements ScoreCardMapper {

    @Autowired
    @Qualifier("delegate")
    private ScoreCardMapper mapper;

    @Override
    public ScoreCard toModel(NewScoreCardResource resource) {
        ScoreCard scoreCard = mapper.toModel(resource);
        var rows = createScoreRows(scoreCard);
        scoreCard.setRows(rows);
        return scoreCard;
    }

    private List<ScoreRow> createScoreRows(ScoreCard scoreCard) {
        List<ScoreRow> rows = new ArrayList<>();

        for (Hole hole : scoreCard.getCourse().getHoles()) {
            ScoreRow row = new ScoreRow();
            row.setHole(hole.getNumber());

            var scores = mapScores(scoreCard.getPlayers(), hole.getPar());
            row.setScores(scores);
            rows.add(row);
        }

        return rows;
    }

    private List<Score> mapScores(List<Player> players, int par) {
        return players.stream()
                .map(EntityModel::getId)
                .map(id -> new Score(id, par))
                .collect(Collectors.toList());
    }
}
