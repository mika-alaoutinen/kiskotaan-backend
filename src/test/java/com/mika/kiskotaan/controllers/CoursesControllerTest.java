package com.mika.kiskotaan.controllers;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Hole;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CoursesControllerTest extends ControllerTest {
    private static final String URL = "/courses";

    @MockBean
    private CourseRepository repository;

    @Test
    public void shouldGetCourses() throws Exception {
        when(repository.findAll()).thenReturn(TestModels.courses());

        MvcResult result = performGet(URL);
        List<Course> courses = parseCourses(result);

        assertCoursesAreSame(courses.get(0), TestModels.courses().get(0));
        assertCoursesAreSame(courses.get(1), TestModels.courses().get(1));
    }

    @Test
    public void shouldAddCourse() throws Exception {
        Course course = TestModels.courses().get(0);
        when(repository.save(any(Course.class))).thenReturn(course);

        MvcResult result = performPost(URL, TestResources.courseResource());
        Course response = parseCourse(result);

        assertCoursesAreSame(course, response);
    }

    public void assertCoursesAreSame(Course c1, Course c2) {
        assertTrue(new ReflectionEquals(c1, "holes").matches(c2));

        for (int i = 0; i < c1.getHoles().size(); i++) {
            assertHolesAreSame(c1.getHoles().get(i), c2.getHoles().get(i));
        }
    }

    private void assertHolesAreSame(Hole h1, Hole h2) {
        assertEquals(h1.getNumber(), h2.getNumber());
        assertEquals(h1.getPar(), h2.getPar());
        assertEquals(h1.getDistance(), h2.getDistance());
    }

    private List<Course> parseCourses(MvcResult result) throws Exception {
        CollectionType collectionType = testUtils.getCollectionType(Course.class);
        String response = testUtils.parseResponseString(result);
        return mapper.readValue(response, collectionType);
    }

    private Course parseCourse(MvcResult result) throws Exception {
        return mapper.readValue(testUtils.parseResponseString(result), Course.class);
    }
}
