package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.dao.GameDao;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.services.GameService;
import kiskotaan.openapi.model.GameResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameDao dao;

    @Override
    public GameResource startGame(Long scoreCardId) {
        return null;
    }

    @Override
    public GameResource endGame(Long scoreCardId) throws NotFoundException {
        return null;
    }
}
