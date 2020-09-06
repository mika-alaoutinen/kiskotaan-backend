package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.mappers.CourseMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.repositories.CourseRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    private static final Long ID = 10L;

    @Mock private CourseMapper mapper;
    @Mock private CourseRepository repository;
    @InjectMocks private CourseServiceImpl service;

    @Test
    public void shouldGetCourses() {
        when(repository.findAll()).thenReturn(TestModels.courses());
        when(mapper.toResource(any(Course.class))).thenReturn(new CourseResource());

        List<CourseResource> courses = service.getCourses();
        assertEquals(2, courses.size());
        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toResource(any(Course.class));
    }

    @Test
    public void shouldAddCourse() {
        NewCourseResource givenResource = new NewCourseResource();
        Course savedCourse = new Course();

        when(mapper.toModel(any(NewCourseResource.class))).thenReturn(new Course());
        when(repository.save(any(Course.class))).thenReturn(savedCourse);
        when(mapper.toResource(any(Course.class))).thenReturn(new CourseResource());

        CourseResource savedResource = service.addCourse(givenResource);
        assertNotNull(savedResource);
        verify(mapper, times(1)).toModel(givenResource);
        verify(repository, times(1)).save(savedCourse);
        verify(mapper, times(1)).toResource(savedCourse);
    }

    @Test
    public void shouldGetCourse() {
        when(repository.findById(ID)).thenReturn(Optional.of(new Course()));
        Course course = service.getCourse(ID);
        assertNotNull(course);
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldThrowExceptionWhenCourseNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        ScoreCardException e = assertThrows(ScoreCardException.class, () -> service.getCourse(ID));
        assertEquals("New score card contains a course that does not exist in database.", e.getMessage());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldReturnTrueWhenExistById() {
        when(repository.existsById(ID)).thenReturn(true);
        assertTrue(service.existsById(ID));
        verify(repository, times(1)).existsById(ID);
    }

    @Test
    public void shouldReturnFalseWhenNotExistById() {
        when(repository.existsById(ID)).thenReturn(false);
        assertFalse(service.existsById(ID));
        verify(repository, times(1)).existsById(ID);
    }
}
