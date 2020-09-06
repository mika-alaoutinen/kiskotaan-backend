package com.mika.kiskotaan.dao.impl;

import com.mika.kiskotaan.dao.CourseDao;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseDaoImpl implements CourseDao {
    private final CourseRepository repository;

    @Override
    public List<Course> getCourses() {
        return repository.findAll();
    }

    @Override
    public Optional<Course> getCourse(Long id) {
        return repository.findById(id);
    }

    @Override
    public Course addCourse(Course newCourse) {
        return repository.save(newCourse);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}
