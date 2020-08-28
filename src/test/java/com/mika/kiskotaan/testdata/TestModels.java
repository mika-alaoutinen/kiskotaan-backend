package com.mika.kiskotaan.testdata;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Hole;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class TestModels {

    public static List<Course> courses() {
        Course c1 = course();

        Course c2 = new Course("Laajavuoren golf", holes(18), 50);
        c2.setId(2L);

        return List.of(c1, c2);
    }

    public static Course course() {
        Course course = new Course("Kuokkalan golf", holes(9), 30);
        course.setId(1L);
        return course;
    }

    public static List<Hole> holes(int numberOfHoles) {
        return IntStream.rangeClosed(1, numberOfHoles)
                .mapToObj(TestModels::hole)
                .collect(Collectors.toList());
    }

    public static Hole hole(int number) {
        Hole hole = new Hole();
        hole.setId((long) number);
        hole.setNumber(number);
        hole.setPar(3);
        return hole;
    }
}
