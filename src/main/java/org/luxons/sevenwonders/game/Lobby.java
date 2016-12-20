package org.luxons.sevenwonders.game;

import java.util.ArrayList;
import java.util.List;

import org.luxons.sevenwonders.errors.UniqueIdAlreadyUsedException;
import org.luxons.sevenwonders.game.data.GameDefinition;

public class Lobby {

    private final long id;

    private final String name;

    private final Player owner;

    private final List<Player> players;

    private final GameDefinition gameDefinition;

    private State state = State.LOBBY;

    public Lobby(long id, String name, Player owner, GameDefinition gameDefinition) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.gameDefinition = gameDefinition;
        this.players = new ArrayList<>(gameDefinition.getMinPlayers());
        players.add(owner);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public synchronized int addPlayer(Player player) throws GameAlreadyStartedException, PlayerOverflowException {
        if (hasStarted()) {
            throw new GameAlreadyStartedException();
        }
        if (maxPlayersReached()) {
            throw new PlayerOverflowException();
        }
        if (playerNameAlreadyUsed(player.getDisplayName())) {
            throw new PlayerNameAlreadyUsedException(player.getDisplayName());
        }
        int playerId = players.size();
        players.add(player);
        return playerId;
    }

    private boolean hasStarted() {
        return state != State.LOBBY;
    }

    private boolean maxPlayersReached() {
        return players.size() >= gameDefinition.getMaxPlayers();
    }

    private boolean playerNameAlreadyUsed(String name) {
        return players.stream().anyMatch(p -> p.getDisplayName().equals(name));
    }

    public synchronized Game startGame(Settings settings) throws PlayerUnderflowException {
        if (!hasEnoughPlayers()) {
            throw new PlayerUnderflowException();
        }
        state = State.PLAYING;
        settings.setNbPlayers(players.size());
        return gameDefinition.initGame(id, settings, players);
    }

    private boolean hasEnoughPlayers() {
        return players.size() >= gameDefinition.getMinPlayers();
    }

    @Override
    public String toString() {
        return "Lobby{" + "id=" + id + ", name='" + name + '\'' + ", state=" + state + '}';
    }

    public boolean isOwner(String userName) {
        return owner.getUserName().equals(userName);
    }

    public boolean containsUser(String userName) {
        return players.stream().anyMatch(p -> p.getUserName().equals(userName));
    }

    private static class GameAlreadyStartedException extends IllegalStateException {
    }

    private static class PlayerOverflowException extends IllegalStateException {
    }

    private static class PlayerUnderflowException extends IllegalStateException {
    }

    private static class PlayerNameAlreadyUsedException extends UniqueIdAlreadyUsedException {
        PlayerNameAlreadyUsedException(String name) {
            super(name);
        }
    }
}
