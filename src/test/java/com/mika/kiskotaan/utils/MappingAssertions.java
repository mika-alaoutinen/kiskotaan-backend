package com.mika.kiskotaan.utils;

import com.mika.kiskotaan.models.*;
import kiskotaan.openapi.model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class MappingAssertions {

    public static void assertCourseMapping(Course model, CourseResource resource) {
        assertEquals(model.getId(), resource.getId().longValue());
        assertEquals(model.getName(), resource.getName());
        for (int i = 0; i < model.getHoles().size(); i++) {
            assertHoleMapping(model.getHoles().get(i), resource.getHoles().get(i));
        }
    }

    public static void assertGameMapping(Game model, GameResource resource) {
        assertEquals(model.isGameOver(), resource.isGameOver());
        assertEquals(model.isScoreChanged(), resource.isScoreChanged());
        assertEquals(model.getHole(), resource.getHole());
    }

    public static void assertHoleMapping(Hole model, HoleResource resource) {
        assertEquals(model.getNumber(), resource.getNumber());
        assertEquals(model.getPar(), resource.getPar());
        assertEquals(model.getDistance(), resource.getDistance());
    }

    public static void assertNewCourseMapping(Course model, NewCourseResource resource) {
        assertEquals(model.getName(), resource.getName());
        for (int i = 0; i < model.getHoles().size(); i++) {
            assertHoleMapping(model.getHoles().get(i), resource.getHoles().get(i));
        }
    }

    public static void assertNewPlayerMapping(Player model, NewPlayerResource resource) {
        assertEquals(model.getName(), resource.getName());
    }

    public static void assertPlayerMapping(Player model, PlayerResource resource) {
        assertEquals(model.getId(), resource.getId().longValue());
        assertEquals(model.getName(), resource.getName());
    }

    public static void assertScoreMapping(Score model, ScoreResource resource) {
        assertEquals(model.getPlayerId(), resource.getPlayerId().longValue());
        assertEquals(model.getScore(), resource.getScore());
    }

    public static void assertScoreRowMapping(ScoreRow model, ScoreRowResource resource) {
        for (int i = 0; i < model.getScores().size(); i++) {
            Score score = model.getScores().get(i);
            ScoreResource scoreResource = resource.getScores().get(i);
            assertScoreMapping(score, scoreResource);
        }
    }
}
