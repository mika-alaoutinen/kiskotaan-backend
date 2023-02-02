package mikaa.players.infra;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mikaa.model.ErrorBodyDTO;
import mikaa.players.errors.BadRequestException;

@RestControllerAdvice
@Slf4j
class GlobalExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  ResponseEntity<ErrorBodyDTO> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
    log.info(ex.getMessage());

    var body = new ErrorBodyDTO()
        .error("Bad Request")
        .message(ex.getMessage())
        .path(req.getRequestURI())
        .status(400)
        .timestamp(OffsetDateTime.now());

    return new ResponseEntity<ErrorBodyDTO>(body, HttpStatus.BAD_REQUEST);
  }

}
