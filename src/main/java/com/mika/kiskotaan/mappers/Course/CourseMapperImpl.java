package com.mika.kiskotaan.mappers.Course;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.HoleResource;
import kiskotaan.openapi.model.NewCourseResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapperImpl implements CourseMapper {
    private final CourseMapStruct mapper;

    public Course toModel(NewCourseResource resource) {
        return mapper.toModel(resource);
    }

    public CourseResource toResource(Course model) {
        CourseResource resource = mapper.toResource(model);
        resource.setHoles(sortHoles(resource.getHoles()));
        return resource;
    }

    private Set<HoleResource> sortHoles(Set<HoleResource> holes) {
        return holes.stream()
                .sorted(Comparator.comparingInt(HoleResource::getNumber))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
