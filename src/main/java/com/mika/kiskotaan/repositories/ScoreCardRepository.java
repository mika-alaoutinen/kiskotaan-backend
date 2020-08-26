package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.ScoreCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreCardRepository extends JpaRepository<ScoreCard, Long> {
}
