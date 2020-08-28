package com.mika.kiskotaan.mappers.Course;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapStruct {
    Course toModel(NewCourseResource resource);
    CourseResource toResource(Course model);

}
