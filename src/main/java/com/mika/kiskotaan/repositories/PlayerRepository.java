package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
