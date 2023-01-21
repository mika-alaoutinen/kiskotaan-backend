package mikaa.kafka.player;

public interface PlayerUpdater {

  void add(PlayerDTO newPlayer);

  void delete(PlayerDTO newPlayer);

  void update(PlayerDTO newPlayer);

}
