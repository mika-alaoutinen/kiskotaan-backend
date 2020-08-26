package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.services.CourseService;
import kiskotaan.openapi.api.CoursesApi;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoursesController implements CoursesApi {
    private final CourseService service;

    @Override
    public ResponseEntity<CourseResource> addCourse(@Valid NewCourseResource newCourseResource) {
        return new ResponseEntity<>(service.addCourse(newCourseResource), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<CourseResource>> getCourses() {
        return ResponseEntity.ok(service.getCourses());
    }
}
