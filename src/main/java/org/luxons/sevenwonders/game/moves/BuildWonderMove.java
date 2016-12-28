package org.luxons.sevenwonders.game.moves;

import java.util.List;

import org.luxons.sevenwonders.game.Settings;
import org.luxons.sevenwonders.game.api.PlayerMove;
import org.luxons.sevenwonders.game.api.Table;
import org.luxons.sevenwonders.game.boards.Board;
import org.luxons.sevenwonders.game.cards.Card;

public class BuildWonderMove extends Move {

    BuildWonderMove(int playerIndex, Card card, PlayerMove move) {
        super(playerIndex, card, move);
    }

    public boolean isValid(Table table) {
        Board board = table.getBoard(getPlayerIndex());
        return board.getWonder().isNextStageBuildable(table, getPlayerIndex(), getBoughtResources());
    }

    @Override
    public void place(Table table, List<Card> discardedCards, Settings settings) {
        table.buildWonderStage(getPlayerIndex(), getCard().getBack());
    }

    @Override
    public void activate(Table table, List<Card> discardedCards, Settings settings) {
        table.activateCurrentWonderStage(getPlayerIndex(), getBoughtResources());
    }
}