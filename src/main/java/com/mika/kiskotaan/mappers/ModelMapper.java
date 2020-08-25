package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModelMapper {

    Course toModel(CourseResource resource);
    CourseResource toResource(Course model);
}
