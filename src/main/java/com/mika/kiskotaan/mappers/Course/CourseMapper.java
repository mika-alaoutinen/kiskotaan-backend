package com.mika.kiskotaan.mappers.Course;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;

public interface CourseMapper {
    Course toModel(NewCourseResource resource);
    CourseResource toResource(Course model);
}
