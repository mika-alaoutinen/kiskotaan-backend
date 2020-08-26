package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
