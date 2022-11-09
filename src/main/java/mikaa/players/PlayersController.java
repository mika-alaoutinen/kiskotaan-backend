package mikaa.players;

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
