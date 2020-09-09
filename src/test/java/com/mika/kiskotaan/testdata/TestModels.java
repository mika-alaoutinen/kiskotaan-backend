package com.mika.kiskotaan.testdata;

import com.mika.kiskotaan.models.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TestModels {

    public static List<Course> courses() {
        Course c1 = course();
        Course c2 = new Course("Laajavuoren golf", holes(18));
        c2.setId(2L);

        return List.of(c1, c2);
    }

    public static Course course() {
        Course course = new Course("Kuokkalan golf", holes(9));
        course.setId(1L);
        return course;
    }

    public static Game game() {
        Game game = new Game(false, false, 1, TestModels.scoreCard());
        game.setId(33L);
        return game;
    }

    public static List<Hole> holes(int numberOfHoles) {
        return IntStream.rangeClosed(1, numberOfHoles)
                .mapToObj(TestModels::hole)
                .collect(Collectors.toList());
    }

    public static Hole hole(int number) {
        Hole hole = new Hole(number, 3, 80);
        hole.setId((long) number);
        return hole;
    }

    public static List<Player> players() {
        Player p1 = player();
        Player p2 = new Player("Kukko Pena");
        p2.setId(2L);
        return List.of(p1, p2);
    }

    public static Player player() {
        Player player = new Player("Pekka Kana");
        player.setId(1L);
        return player;
    }

    public static ScoreCard scoreCard() {
        ScoreCard scoreCard = new ScoreCard(course(), players(), scoreRows());
        scoreCard.setId(22L);
        return scoreCard;
    }

    public static ScoreRow scoreRow(int hole) {
        return new ScoreRow(hole, scores());
    }

    public static List<Score> scores() {
        Score s1 = new Score(1L, 3);
        Score s2 = new Score(2L, 3);
        return List.of(s1, s2);
    }

    private static List<ScoreRow> scoreRows() {
        return IntStream.rangeClosed(1, 9)
                .mapToObj(TestModels::scoreRow)
                .collect(Collectors.toList());
    }
}
