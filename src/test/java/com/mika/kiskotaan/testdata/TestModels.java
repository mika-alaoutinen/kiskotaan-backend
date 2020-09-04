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
        Player p1 = new Player("Kukko Pena");
        p1.setId(2L);
        Player p2 = player();
        return List.of(p1, p2);
    }

    public static Player player() {
        Player player = new Player("Pekka Kana");
        player.setId(1L);
        return player;
    }

    public static ScoreCard scoreCard() {
        return new ScoreCard(course(), players(), scoreRows());
    }

    public static ScoreRow scoreRow(int hole) {
        return new ScoreRow(hole, scores());
    }

    private static List<ScoreRow> scoreRows() {
        ScoreRow row1 = scoreRow(1);
        ScoreRow row2 = new ScoreRow(2, scores());
        return List.of(row1, row2);
    }

    public static List<Score> scores() {
        Score s1 = new Score(1L, 3);
        Score s2 = new Score(2L, 4);
        return List.of(s1, s2);
    }
}
