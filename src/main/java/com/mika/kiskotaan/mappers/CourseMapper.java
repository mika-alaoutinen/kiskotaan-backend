package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    Course toModel(CourseResource resource);
    Course toModel(NewCourseResource resource);
    CourseResource toResource(Course model);
}
