package com.mika.kiskotaan.testdata;

import com.mika.kiskotaan.models.Course;

import java.util.List;

public abstract class TestModels {

    public static List<Course> courses() {
        Course c1 = new Course("Kuokkalan golf", 30);
        c1.setId(1L);

        Course c2 = new Course("Laajavuoren golf", 50);
        c2.setId(2L);

        return List.of(c1, c2);
    }
}
