package com.mika.kiskotaan.testdata;

import kiskotaan.openapi.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TestResources {

    public static CourseResource courseResource() {
        return new CourseResource()
                .id(new BigDecimal(1))
                .name("Kuokkalan golf")
                .holes(holeResources(18));
    }

    public static NewCourseResource newCourseResource() {
        return new NewCourseResource()
                .name("Uusi rata")
                .holes(holeResources(9));
    }

    public static GameResource gameResource() {
        return new GameResource()
                .scoreChanged(false)
                .gameOver(false)
                .hole(1)
                .scoreCardId(new BigDecimal(1));
    }

    public static List<HoleResource> holeResources(int numberOfHoles) {
        return IntStream.rangeClosed(1, numberOfHoles)
                .mapToObj(TestResources::holeResource)
                .collect(Collectors.toList());
    }

    public static HoleResource holeResource(int number) {
        return new HoleResource()
                .number(number)
                .par(3)
                .distance(80);
    }

    public static List<PlayerResource> playerResources() {
        PlayerResource p1 = playerResource();
        PlayerResource p2 = new PlayerResource()
                .id(new BigDecimal(2))
                .name("Kukko Pena");

        return List.of(p1, p2);
    }

    public static PlayerResource playerResource() {
        return new PlayerResource()
                .id(new BigDecimal(1))
                .name("Pekka Kana");
    }

    public static NewPlayerResource newPlayerResource() {
        return new NewPlayerResource()
                .name("Kukko Pena");
    }

    public static ScoreCardResource scoreCardResource() {
        return new ScoreCardResource()
                .id(new BigDecimal(1))
                .course(courseResource())
                .players(playerResources())
                .rows(rows());
    }

    public static NewScoreCardResource newScoreCardResource() {
        return new NewScoreCardResource()
                .courseId(new BigDecimal(1L))
                .playersIds(Set.of(new BigDecimal(2), new BigDecimal(3)));
    }

    public static ScoreRowResource scoreRowResource(int score) {
        return new ScoreRowResource()
                .hole(1)
                .scores(scores(score));
    }

    public static List<ScoreRowResource> rows() {
        ScoreRowResource row1 = scoreRowResource(3);

        ScoreRowResource row2 = new ScoreRowResource()
                .hole(2)
                .scores(scores(4));

        return List.of(row1, row2);
    }

    public static List<ScoreResource> scores(int score) {
        ScoreResource score1 = new ScoreResource()
                .playerId(new BigDecimal(1))
                .score(score);

        ScoreResource score2 = new ScoreResource()
                .playerId(new BigDecimal(2))
                .score(score + 1);

        return List.of(score1, score2);
    }
}
