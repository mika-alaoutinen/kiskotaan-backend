package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Hole;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.HoleResource;
import kiskotaan.openapi.model.NewCourseResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CourseMapperTest {

    @Autowired
    private CourseMapper mapper;

    @Test
    public void shouldMapNewResourceToModel() {
        Course model = mapper.toModel(TestResources.newCourseResource());
        assertNewCourseMappingMappingOk(model, TestResources.newCourseResource());
    }

    @Test
    public void shouldMapResourceToModel() {
        Course model = mapper.toModel(TestResources.courseResource());
        assertCourseMappingMappingOk(model, TestResources.courseResource());
    }

    @Test
    public void shouldMapModelToResource() {
        CourseResource resource = mapper.toResource(TestModels.course());
        assertCourseMappingMappingOk(TestModels.course(), resource);
    }

    public void assertCourseMappingMappingOk(Course model, CourseResource resource) {
        assertEquals(model.getId(), resource.getId().longValue());
        assertEquals(model.getName(), resource.getName());
        assertMappedHolesOk(model.getHoles(), new ArrayList<>(resource.getHoles()));
    }

    private void assertNewCourseMappingMappingOk(Course model, NewCourseResource resource) {
        assertEquals(model.getName(), resource.getName());
        assertMappedHolesOk(model.getHoles(), new ArrayList<>(resource.getHoles()));
    }

    private void assertMappedHolesOk(List<Hole> models, List<HoleResource> resources) {
        for (int i = 0; i < resources.size(); i++) {
            assertMappedHoleOk(models.get(i), resources.get(i));
        }
    }

    private void assertMappedHoleOk(Hole model, HoleResource resource) {
        assertEquals(model.getNumber(), resource.getNumber());
        assertEquals(model.getPar(), resource.getPar());
        assertEquals(model.getDistance(), resource.getDistance());
    }
}
