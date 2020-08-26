package com.mika.kiskotaan.services;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;

import java.util.List;

public interface CourseService {
    List<CourseResource> getCourses();
    CourseResource addCourse(CourseResource resource);
}