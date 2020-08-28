package com.mika.kiskotaan.services;

import com.mika.kiskotaan.mappers.Course.CourseMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.services.impl.CourseServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseMapper mapper;

    @Mock
    private CourseRepository repository;

    @InjectMocks
    private CourseServiceImpl service;

    @BeforeEach
    public void setup() {
        when(mapper.toResource(any(Course.class))).thenReturn(new CourseResource());
    }

    @Test
    public void shouldGetCourses() {
        when(repository.findAll()).thenReturn(TestModels.courses());

        List<CourseResource> resources = service.getCourses();

        assertEquals(2, resources.size());
        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toResource(any(Course.class));
    }

    @Test
    public void shouldAddCourse() {
        NewCourseResource givenResource = new NewCourseResource();
        Course savedCourse = new Course();

        when(mapper.toModel(any(NewCourseResource.class))).thenReturn(new Course());
        when(repository.save(any(Course.class))).thenReturn(savedCourse);

        CourseResource savedResource = service.addCourse(givenResource);

        assertNotNull(savedResource);
        verify(repository, times(1)).save(savedCourse);
        verify(mapper, times(1)).toModel(givenResource);
        verify(mapper, times(1)).toResource(savedCourse);
    }
}
