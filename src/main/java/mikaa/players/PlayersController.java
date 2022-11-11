package mikaa.players;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class PlayersController {

  @GetMapping("hello")
  ResponseEntity<String> hello() {
    return ResponseEntity.ok("Hello");
  }
}
