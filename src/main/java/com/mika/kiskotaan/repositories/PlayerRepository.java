package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsAllByIdIn(Set<Long> playerIds);
}
