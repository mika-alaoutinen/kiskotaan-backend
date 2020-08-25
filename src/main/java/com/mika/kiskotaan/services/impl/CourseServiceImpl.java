package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository repository;
}
