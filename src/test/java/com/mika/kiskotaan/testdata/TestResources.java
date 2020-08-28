package com.mika.kiskotaan.testdata;

import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.HoleResource;
import kiskotaan.openapi.model.NewCourseResource;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TestResources {

    public static CourseResource courseResource() {
        return new CourseResource()
                .id("1")
                .name("Kuokkalan golf")
                .par(30);
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
}
