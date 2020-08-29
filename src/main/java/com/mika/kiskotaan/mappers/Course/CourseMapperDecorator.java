package com.mika.kiskotaan.mappers.Course;

import com.mika.kiskotaan.models.Course;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.HoleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CourseMapperDecorator implements CourseMapper {

    @Autowired
    @Qualifier("delegate")
    private CourseMapper mapper;

    @Override
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
