package com.mika.kiskotaan.services;

import com.mika.kiskotaan.dao.CourseDao;
import com.mika.kiskotaan.mappers.CourseMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.services.impl.CourseServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
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

    @Mock private CourseMapper mapper;
    @Mock private CourseDao dao;
    @InjectMocks private CourseServiceImpl service;

    @Test
    public void shouldGetCourses() {
        when(dao.getCourses()).thenReturn(TestModels.courses());
        when(mapper.toResource(any(Course.class))).thenReturn(new CourseResource());

        List<CourseResource> courses = service.getCourses();
        assertEquals(2, courses.size());
        verify(dao, times(1)).getCourses();
        verify(mapper, times(2)).toResource(any(Course.class));
    }

    @Test
    public void shouldAddCourse() {
        NewCourseResource givenResource = new NewCourseResource();
        Course savedCourse = new Course();

        when(mapper.toModel(any(NewCourseResource.class))).thenReturn(new Course());
        when(dao.addCourse(any(Course.class))).thenReturn(savedCourse);
        when(mapper.toResource(any(Course.class))).thenReturn(new CourseResource());

        CourseResource savedResource = service.addCourse(givenResource);
        assertNotNull(savedResource);
        verify(mapper, times(1)).toModel(givenResource);
        verify(dao, times(1)).addCourse(savedCourse);
        verify(mapper, times(1)).toResource(savedCourse);
    }
}
