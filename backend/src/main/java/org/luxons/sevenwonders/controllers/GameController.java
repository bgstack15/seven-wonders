package org.luxons.sevenwonders.controllers;

import java.security.Principal;

import org.luxons.sevenwonders.actions.PrepareCardAction;
import org.luxons.sevenwonders.game.Game;
import org.luxons.sevenwonders.lobby.Lobby;
import org.luxons.sevenwonders.lobby.Player;
import org.luxons.sevenwonders.game.api.PlayerTurnInfo;
import org.luxons.sevenwonders.output.PreparedCard;
import org.luxons.sevenwonders.game.cards.CardBack;
import org.luxons.sevenwonders.repositories.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final SimpMessagingTemplate template;

    private final PlayerRepository playerRepository;

    @Autowired
    public GameController(SimpMessagingTemplate template, PlayerRepository playerRepository) {
        this.template = template;
        this.playerRepository = playerRepository;
    }

    @MessageMapping("/game/{gameId}/prepare")
    public void prepareCard(@DestinationVariable long gameId, PrepareCardAction action, Principal principal) {
        Player player = playerRepository.find(principal.getName());
        Game game = player.getGame();
        CardBack preparedCardBack = game.prepareCard(player.getIndex(), action.getMove());
        PreparedCard preparedCard = new PreparedCard(player, preparedCardBack);
        logger.info("Game '{}': player {} prepared move {}", gameId, principal.getName(), action.getMove());

        if (game.areAllPlayersReady()) {
            game.playTurn();
            sendTurnInfo(player.getLobby(), game);
        } else {
            sendPreparedCard(preparedCard, game);
        }
    }

    private void sendPreparedCard(PreparedCard preparedCard, Game game) {
        template.convertAndSend("/topic/game/" + game.getId() + "/prepared", preparedCard);
    }

    private void sendTurnInfo(Lobby lobby, Game game) {
        for (PlayerTurnInfo turnInfo : game.getCurrentTurnInfo()) {
            Player player = lobby.getPlayers().get(turnInfo.getPlayerIndex());
            template.convertAndSendToUser(player.getUsername(), "/queue/game/turn", turnInfo);
        }
    }
}