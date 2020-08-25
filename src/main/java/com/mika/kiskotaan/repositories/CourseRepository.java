package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
}
