package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.models.Course;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    List<Course> getCourses();
    Optional<Course> getCourse(Long id);
    Course addCourse(Course newCourse);
    boolean existsById(Long id);
}
