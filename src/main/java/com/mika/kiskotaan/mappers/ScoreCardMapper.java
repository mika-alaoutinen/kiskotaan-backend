package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.*;
import kiskotaan.openapi.model.ScoreCardResource;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = { CourseMapper.class, HoleMapper.class, PlayerMapper.class, ScoreRowMapper.class, ScoreMapper.class }
)
public interface ScoreCardMapper {
    ScoreCard toModel(ScoreCardResource resource);
    ScoreCardResource toResource(ScoreCard model);

    default ScoreCard toScoreCard(Course course, List<Player> players) {
        List<ScoreRow> rows = initScoreRows(course, players);
        return new ScoreCard(course, players, rows);
    }

    private List<ScoreRow> initScoreRows(final Course course, final List<Player> players) {
        return course.getHoles().stream()
                .map(hole -> mapScoreRow(hole, players))
                .collect(Collectors.toList());
    }

    private ScoreRow mapScoreRow(Hole hole, List<Player> players) {
        var scores = mapScores(players, hole.getPar());
        return new ScoreRow(hole.getNumber(), scores);
    }

    private List<Score> mapScores(List<Player> players, int par) {
        return players.stream()
                .map(EntityModel::getId)
                .map(id -> new Score(id, par))
                .collect(Collectors.toList());
    }
}