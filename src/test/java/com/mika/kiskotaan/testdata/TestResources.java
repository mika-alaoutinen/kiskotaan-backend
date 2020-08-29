package com.mika.kiskotaan.testdata;

import kiskotaan.openapi.model.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
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

    public static Set<HoleResource> holeResources(int numberOfHoles) {
        return IntStream.rangeClosed(1, numberOfHoles)
                .mapToObj(TestResources::holeResource)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static HoleResource holeResource(int number) {
        return new HoleResource()
                .number(number)
                .par(3)
                .distance(80);
    }

    public static Set<PlayerResource> playerResources() {
        PlayerResource p1 = playerResource();
        PlayerResource p2 = new PlayerResource()
                .id(new BigDecimal(2))
                .name("Kukko Pena");

        return Set.of(p1, p2);
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

    public static NewScoreCardResource newScoreCardResource() {
        return new NewScoreCardResource()
                .course(courseResource())
                .players(playerResources());
    }
}
