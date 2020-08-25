package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.repositories.GameRepository;
import com.mika.kiskotaan.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository repository;
}
