package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
