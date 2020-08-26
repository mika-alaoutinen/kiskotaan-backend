package com.mika.kiskotaan.testdata;

import kiskotaan.openapi.model.CourseResource;

public abstract class TestResources {

    public static CourseResource courseResource() {
        return new CourseResource()
                .id("1")
                .name("Kuokkalan golf")
                .par(30);
    }
}
