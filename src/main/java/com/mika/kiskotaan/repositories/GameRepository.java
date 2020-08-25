package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
}
