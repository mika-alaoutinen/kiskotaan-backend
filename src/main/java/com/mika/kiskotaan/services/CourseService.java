package com.mika.kiskotaan.services;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;

import java.math.BigDecimal;
import java.util.List;

public interface CourseService {
    List<CourseResource> getCourses();
    CourseResource addCourse(NewCourseResource resource);

    Course getCourse(Long id);
    boolean existsById(Long id);
}