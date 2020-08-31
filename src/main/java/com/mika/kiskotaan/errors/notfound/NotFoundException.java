package com.mika.kiskotaan.errors.notfound;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(Player player, Long id) {
        super(message("player", id));
    }

    public NotFoundException(ScoreCard scoreCard, Long id) {
        super(message("score card", id));
    }

    public NotFoundException(ScoreRow row, Long id) {
        super(message("score row", id));
    }

    private static String message(String item, Long id) {
        return String.format("Could not find %s with ID %s", item, id);
    }
}
