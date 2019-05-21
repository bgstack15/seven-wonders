package org.luxons.sevenwonders.game.cards

import org.luxons.sevenwonders.game.Player
import org.luxons.sevenwonders.game.api.cards.CardBack
import org.luxons.sevenwonders.game.api.cards.CardPlayability
import org.luxons.sevenwonders.game.api.cards.Color
import org.luxons.sevenwonders.game.api.cards.PlayabilityLevel
import org.luxons.sevenwonders.game.boards.Board
import org.luxons.sevenwonders.game.effects.Effect
import org.luxons.sevenwonders.game.api.resources.ResourceTransactions
import org.luxons.sevenwonders.game.api.resources.noTransactions

internal data class Card(
    val name: String,
    val color: Color,
    val requirements: Requirements,
    val effects: List<Effect>,
    val chainParent: String?,
    val chainChildren: List<String>,
    val image: String,
    val back: CardBack
) {
    fun computePlayabilityBy(player: Player): CardPlayability = when {
        isAlreadyOnBoard(player.board) -> Playability.incompatibleWithBoard() // cannot play twice the same card
        isParentOnBoard(player.board) -> Playability.chainable()
        else -> Playability.requirementDependent(requirements.assess(player))
    }

    fun isPlayableOnBoardWith(board: Board, transactions: ResourceTransactions) =
        isChainableOn(board) || requirements.areMetWithHelpBy(board, transactions)

    private fun isChainableOn(board: Board): Boolean = !isAlreadyOnBoard(board) && isParentOnBoard(board)

    private fun isAlreadyOnBoard(board: Board): Boolean = board.isPlayed(name)

    private fun isParentOnBoard(board: Board): Boolean = chainParent != null && board.isPlayed(chainParent)

    fun applyTo(player: Player, transactions: ResourceTransactions) {
        if (!isChainableOn(player.board)) {
            requirements.pay(player, transactions)
        }
        effects.forEach { it.applyTo(player) }
    }
}

private object Playability {

    internal fun incompatibleWithBoard(): CardPlayability =
        CardPlayability(
            isPlayable = false,
            playabilityLevel = PlayabilityLevel.INCOMPATIBLE_WITH_BOARD
        )

    internal fun chainable(): CardPlayability =
        CardPlayability(
            isPlayable = true,
            isChainable = true,
            minPrice = 0,
            cheapestTransactions = setOf(noTransactions()),
            playabilityLevel = PlayabilityLevel.CHAINABLE
        )

    internal fun requirementDependent(satisfaction: RequirementsSatisfaction): CardPlayability =
        CardPlayability(
            isPlayable = satisfaction.satisfied,
            isChainable = false,
            minPrice = satisfaction.minPrice,
            cheapestTransactions = satisfaction.cheapestTransactions,
            playabilityLevel = satisfaction.level
        )
}
