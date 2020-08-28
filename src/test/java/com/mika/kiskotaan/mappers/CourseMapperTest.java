package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.mappers.Course.CourseMapStruct;
import com.mika.kiskotaan.mappers.Course.CourseMapper;
import com.mika.kiskotaan.mappers.Course.CourseMapperImpl;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Hole;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.HoleResource;
import kiskotaan.openapi.model.NewCourseResource;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class CourseMapperTest {
    private final CourseMapper mapper = new CourseMapperImpl(Mappers.getMapper(CourseMapStruct.class));

    @Test
    public void shouldMapToModel() {
        NewCourseResource resource = TestResources.newCourseResource();
        Course course = mapper.toModel(resource);
        assertMappingOk(course, resource);
    }

    @Test
    public void shouldMapToResource() {
        Course model = TestModels.course();
        CourseResource resource = mapper.toResource(model);
        assertMappingOk(model, resource);
    }

    private void assertMappingOk(Course model, NewCourseResource resource) {
        assertEquals(model.getName(), resource.getName());
        assertEquals(model.getPar(), calculateCoursePar(resource.getHoles()));
        assertMappedHolesOk(model.getHoles(), new ArrayList<>(resource.getHoles()));
    }

    private void assertMappingOk(Course model, CourseResource resource) {
        assertEquals(model.getId().toString(), resource.getId());
        assertEquals(model.getName(), resource.getName());
        assertEquals(model.getPar(), resource.getPar());
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

    private int calculateCoursePar(Set<HoleResource> holes) {
        return holes.stream()
                .mapToInt(HoleResource::getPar)
                .sum();
    }
}
