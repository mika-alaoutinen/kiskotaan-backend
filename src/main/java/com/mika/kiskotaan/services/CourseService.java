package com.mika.kiskotaan.services;

import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;

import java.util.List;

public interface CourseService {
    List<CourseResource> getCourses();
    CourseResource addCourse(NewCourseResource resource);
}