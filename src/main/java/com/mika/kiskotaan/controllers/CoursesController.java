package com.mika.kiskotaan.controllers;

import kiskotaan.openapi.api.CoursesApi;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CoursesController implements CoursesApi {

    @Override
    public ResponseEntity<CourseResource> addCourse(@Valid NewCourseResource newCourseResource) {
        return null;
    }

    @Override
    public ResponseEntity<List<CourseResource>> getCourses() {
        return null;
    }
}
