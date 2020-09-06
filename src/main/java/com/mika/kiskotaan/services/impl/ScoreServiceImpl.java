package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.dao.ScoreCardDao;
import com.mika.kiskotaan.errors.badrequest.ScoreRowException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreRowMapper;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.services.ScoreService;
import kiskotaan.openapi.model.ScoreRowResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final ScoreCardDao dao;
    private final ScoreRowMapper mapper;

    @Override
    public ScoreRowResource editScoreRow(Long scoreCardId, ScoreRowResource resource)
            throws NotFoundException, ScoreRowException {

        ScoreCard scoreCard = findScoreCard(scoreCardId);
        ScoreRow rowToUpdate = findRow(scoreCard.getRows(), resource);

        mapper.editScoreRow(resource, rowToUpdate);
        dao.updateScoreCard(scoreCard);

        return mapper.toResources(rowToUpdate);
    }

    private ScoreCard findScoreCard(Long id) throws NotFoundException {
        return dao.getScoreCard(id)
                .orElseThrow(() -> new NotFoundException(new ScoreCard(), id));
    }

    private ScoreRow findRow(List<ScoreRow> rows, ScoreRowResource resource) throws ScoreRowException {
        return rows.stream()
                .filter(row -> row.getHole() == resource.getHole())
                .findAny()
                .orElseThrow(() -> new ScoreRowException(resource));
    }
}