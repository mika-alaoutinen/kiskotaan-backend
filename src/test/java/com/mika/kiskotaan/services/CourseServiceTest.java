package com.mika.kiskotaan.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mika.kiskotaan.TestData;
import com.mika.kiskotaan.mappers.CourseMapper;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.services.impl.CourseServiceImpl;
import kiskotaan.openapi.model.CourseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    private final CourseMapper mapper = Mappers.getMapper(CourseMapper.class);

    @Mock
    private CourseRepository repository;

//    @InjectMocks
    private CourseService service;

    @BeforeEach
    public void setup() {
        this.service = new CourseServiceImpl(mapper, repository);
    }

    @Test
    public void shouldGetCourses() {
        when(repository.findAll()).thenReturn(TestData.courses());

        List<CourseResource> resources = service.getCourses();
        System.out.println(resources.get(0));

        verify(repository, times(1)).findAll();
    }
}
