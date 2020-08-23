package com.mika.kiskotaan.controllers;

import kiskotaan.openapi.api.CoursesApi;
import kiskotaan.openapi.model.Course;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

public class CoursesController implements CoursesApi {

    @Override
    public ResponseEntity<List<String>> getCourses() {
        return null;
    }

    @Override
    public ResponseEntity<Course> postCourse(@Valid Course course) {
        return null;
    }

}
