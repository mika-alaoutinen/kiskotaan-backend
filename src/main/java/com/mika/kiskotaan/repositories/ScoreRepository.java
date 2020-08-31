package com.mika.kiskotaan.repositories;

import com.mika.kiskotaan.models.ScoreRow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<ScoreRow, Long> {
}
