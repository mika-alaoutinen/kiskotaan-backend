package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.dao.impl.CourseDaoImpl;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseDaoTest {

    private static final Long ID = 10L;
    private static final Course COURSE = new Course();

    @Mock private CourseRepository repository;
    @InjectMocks private CourseDaoImpl dao;

    @Test
    public void shouldGetCourses() {
        when(repository.findAll()).thenReturn(TestModels.courses());
        assertEquals(2, dao.getCourses().size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void shouldGetCourse() {
        when(repository.findById(ID)).thenReturn(Optional.of(COURSE));
        Course course = dao.getCourse(ID).orElse(null);
        assertNotNull(course);
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldHandleCourseNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        assertTrue(dao.getCourse(ID).isEmpty());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldAddCourse() {
        when(repository.save(COURSE)).thenReturn(COURSE);
        assertEquals(COURSE, dao.addCourse(COURSE));
        verify(repository, times(1)).save(COURSE);
    }

    @Test
    public void shouldReturnTrueWhenExistById() {
        when(repository.existsById(ID)).thenReturn(true);
        assertTrue(dao.existsById(ID));
        verify(repository, times(1)).existsById(ID);
    }

    @Test
    public void shouldReturnFalseWhenNotExistById() {
        when(repository.existsById(ID)).thenReturn(false);
        assertFalse(dao.existsById(ID));
        verify(repository, times(1)).existsById(ID);
    }
}
