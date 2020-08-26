package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        assertEquals(courses.get(0), TestModels.courses().get(0));
        assertEquals(courses.get(1), TestModels.courses().get(1));
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
        assertEquals(response, course);
    }
}
