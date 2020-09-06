package com.mika.kiskotaan.dao.impl;

import com.mika.kiskotaan.dao.ScoreCardDao;
import com.mika.kiskotaan.models.*;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreCardDaoImpl implements ScoreCardDao {
    private final ScoreCardRepository repository;

    @Override
    public Optional<ScoreCard> getScoreCard(Long id) {
        return repository.findById(id);
    }

    @Override
    public ScoreCard addScoreCard(ScoreCard newScoreCard) {
        ScoreCard scoreCard = toScoreCard(newScoreCard.getCourse(), newScoreCard.getPlayers());
        return repository.save(scoreCard);
    }

    @Override
    public void deleteScoreCard(Long id) {
        repository.deleteById(id);
    }

    // TODO: this is public to enable unit testing. Figure out a smarter way to test this.
    public ScoreCard toScoreCard(Course course, List<Player> players) {
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
