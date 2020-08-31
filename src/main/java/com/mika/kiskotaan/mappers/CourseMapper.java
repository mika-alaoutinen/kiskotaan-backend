package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = HoleMapper.class
)
public interface CourseMapper {
    Course toModel(NewCourseResource resource);
    Course toModel(CourseResource resource);
    CourseResource toResource(Course model);
}
