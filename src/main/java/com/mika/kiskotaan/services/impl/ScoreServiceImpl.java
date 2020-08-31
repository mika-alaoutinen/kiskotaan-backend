package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreMapper;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.repositories.ScoreRepository;
import com.mika.kiskotaan.services.ScoreService;
import kiskotaan.openapi.model.ScoreRowResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreMapper mapper;
    private final ScoreRepository repository;

    @Override
    public ScoreRowResource editScoreRow(Long id, ScoreRowResource resource) {
        return repository.findById(id)
                .map(row -> mapper.editRow(resource, row))
                .map(repository::save)
                .map(mapper::toResources)
                .orElseThrow(() -> new NotFoundException(new ScoreRow(), id));
    }
}
