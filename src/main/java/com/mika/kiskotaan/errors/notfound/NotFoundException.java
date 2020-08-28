package com.mika.kiskotaan.errors.notfound;

import com.mika.kiskotaan.models.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(Player player, Long id) {
        super("Could not find player with id " + id);
    }
}
