package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.*;
import kiskotaan.openapi.model.NewScoreCardResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ScoreCardMapperDecorator implements ScoreCardMapper {

    @Autowired
    @Qualifier("delegate")
    private ScoreCardMapper mapper;

    @Override
    public ScoreCard toModel(NewScoreCardResource resource) {
        ScoreCard scoreCard = mapper.toModel(resource);
        scoreCard.setRows(initScoreRows(scoreCard));
        return scoreCard;
    }

    private List<ScoreRow> initScoreRows(ScoreCard scoreCard) {
        return scoreCard.getCourse().getHoles().stream()
                .map(hole -> mapScoreRow(hole, scoreCard.getPlayers()))
                .collect(Collectors.toList());
    }

    private ScoreRow mapScoreRow(Hole hole, List<Player> players) {
        ScoreRow row = new ScoreRow();

        var scores = mapScores(players, hole.getPar());
        row.setScores(scores);
        row.setHole(hole.getNumber());

        return row;
    }

    private List<Score> mapScores(List<Player> players, int par) {
        return players.stream()
                .map(EntityModel::getId)
                .map(id -> new Score(id, par))
                .collect(Collectors.toList());
    }
}
