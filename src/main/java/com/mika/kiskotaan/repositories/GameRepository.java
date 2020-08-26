package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
