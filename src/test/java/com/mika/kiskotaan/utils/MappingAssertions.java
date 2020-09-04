package com.mika.kiskotaan.utils;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Hole;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.HoleResource;
import kiskotaan.openapi.model.NewCourseResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class MappingAssertions {

    public static void assertCourseMapping(Course model, CourseResource resource) {
        assertEquals(model.getId(), resource.getId().longValue());
        assertEquals(model.getName(), resource.getName());
        for (int i = 0; i < model.getHoles().size(); i++) {
            assertHoleMapping(model.getHoles().get(i), resource.getHoles().get(i));
        }
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
}
