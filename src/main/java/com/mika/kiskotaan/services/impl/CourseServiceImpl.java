package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.mappers.CourseMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.services.CourseService;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseMapper mapper;
    private final CourseRepository repository;

    public List<CourseResource> getCourses() {
        return repository.findAll().stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }

    public CourseResource addCourse(NewCourseResource resource) {
        Course newCourse = repository.save(mapper.toModel(resource));
        return mapper.toResource(newCourse);
    }
}
