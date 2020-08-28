package com.mika.kiskotaan.mappers.Course;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.HoleResource;
import kiskotaan.openapi.model.NewCourseResource;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CourseMapperImpl implements CourseMapper {
    private final CourseMapStruct mapper;

    public Course toModel(NewCourseResource resource) {
        Course course = mapper.toModel(resource);
        course.setPar(calculateCoursePar(resource.getHoles()));
        return course;
    }

    public CourseResource toResource(Course model) {
        CourseResource resource = mapper.toResource(model);
        resource.setHoles(sortHoles(resource.getHoles()));
        return resource;
    }

    private int calculateCoursePar(Set<HoleResource> holes) {
        return holes.stream()
                .mapToInt(HoleResource::getPar)
                .sum();
    }

    private Set<HoleResource> sortHoles(Set<HoleResource> holes) {
        return holes.stream()
                .sorted(Comparator.comparingInt(HoleResource::getNumber))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
