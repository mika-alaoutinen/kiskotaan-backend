package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewCourseResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CourseMapperTest {

    private static final Course COURSE = TestModels.course();
    private static final CourseResource COURSE_RESOURCE = TestResources.courseResource();
    private static final NewCourseResource NEW_COURSE_RESOURCE = TestResources.newCourseResource();

    @Autowired private CourseMapper mapper;

    @Test
    public void shouldMapNewResourceToModel() {
        Course mapped = mapper.toModel(NEW_COURSE_RESOURCE);
        System.out.println(mapped);
        MappingAssertions.assertNewCourseMapping(mapped, NEW_COURSE_RESOURCE);
    }

    @Test
    public void shouldMapResourceToModel() {
        Course mapped = mapper.toModel(COURSE_RESOURCE);
        MappingAssertions.assertCourseMapping(mapped, COURSE_RESOURCE);
    }

    @Test
    public void shouldMapModelToResource() {
        CourseResource mapped = mapper.toResource(COURSE);
        MappingAssertions.assertCourseMapping(COURSE, mapped);
    }
}
