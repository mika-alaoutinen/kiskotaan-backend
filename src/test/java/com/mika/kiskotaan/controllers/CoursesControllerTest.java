package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Hole;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CoursesControllerTest extends ControllerTest {
    private static final String url = "/courses";

    @MockBean
    private CourseRepository repository;

    @Test
    public void shouldGetCourses() throws Exception {
        when(repository.findAll()).thenReturn(TestModels.courses());

        MvcResult result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        List<Course> courses = testUtils.parseModels(result, Course.class);

        assertCoursesAreSame(courses.get(0), TestModels.courses().get(0));
        assertCoursesAreSame(courses.get(1), TestModels.courses().get(1));
    }

    @Test
    public void shouldAddCourse() throws Exception {
        Course course = TestModels.courses().get(0);
        Object courseResource = TestResources.courseResource();

        when(repository.save(any(Course.class))).thenReturn(course);

        MvcResult result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.writeModel(courseResource)))
            .andExpect(status().isCreated())
            .andReturn();

        Course response = testUtils.parseModel(result, Course.class);
        assertCoursesAreSame(course, response);
    }

    private void assertCoursesAreSame(Course c1, Course c2) {
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
}
