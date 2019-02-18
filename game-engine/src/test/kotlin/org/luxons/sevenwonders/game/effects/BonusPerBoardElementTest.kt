package org.luxons.sevenwonders.game.effects

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith
import org.luxons.sevenwonders.game.Player
import org.luxons.sevenwonders.game.SimplePlayer
import org.luxons.sevenwonders.game.api.Table
import org.luxons.sevenwonders.game.boards.RelativeBoardPosition
import org.luxons.sevenwonders.game.cards.CardBack
import org.luxons.sevenwonders.game.cards.Color
import org.luxons.sevenwonders.game.test.addCards
import org.luxons.sevenwonders.game.test.testTable

@RunWith(Theories::class)
class BonusPerBoardElementTest {

    private var table: Table? = null
    private var player0: Player? = null

    @Before
    fun setUp() {
        table = testTable(4)
        player0 = SimplePlayer(0, table!!)
    }

    @Theory
    fun computePoints_countsCards(
        boardPosition: RelativeBoardPosition,
        nbCards: Int,
        nbOtherCards: Int,
        points: Int,
        gold: Int,
        color: Color
    ) {
        val board = table!!.getBoard(0, boardPosition)
        addCards(board, nbCards, nbOtherCards, color)

        val bonus = BonusPerBoardElement(listOf(boardPosition), BoardElementType.CARD, gold, points, listOf(color))

        assertEquals((nbCards * points).toLong(), bonus.computePoints(player0!!).toLong())
    }

    @Theory
    fun computePoints_countsDefeatTokens(
        boardPosition: RelativeBoardPosition,
        nbDefeatTokens: Int,
        points: Int,
        gold: Int
    ) {
        val board = table!!.getBoard(0, boardPosition)
        for (i in 0 until nbDefeatTokens) {
            board.military.defeat()
        }

        val bonus = BonusPerBoardElement(listOf(boardPosition), BoardElementType.DEFEAT_TOKEN, gold, points, listOf())

        assertEquals((nbDefeatTokens * points).toLong(), bonus.computePoints(player0!!).toLong())
    }

    @Theory
    fun computePoints_countsWonderStages(boardPosition: RelativeBoardPosition, nbStages: Int, points: Int, gold: Int) {
        val board = table!!.getBoard(0, boardPosition)
        for (i in 0 until nbStages) {
            board.wonder.placeCard(CardBack(""))
        }

        val bonus =
            BonusPerBoardElement(listOf(boardPosition), BoardElementType.BUILT_WONDER_STAGES, gold, points, listOf())

        assertEquals((nbStages * points).toLong(), bonus.computePoints(player0!!).toLong())
    }

    @Theory
    fun apply_countsCards(
        boardPosition: RelativeBoardPosition,
        nbCards: Int,
        nbOtherCards: Int,
        points: Int,
        gold: Int,
        color: Color
    ) {
        val board = table!!.getBoard(0, boardPosition)
        addCards(board, nbCards, nbOtherCards, color)

        val bonus = BonusPerBoardElement(listOf(boardPosition), BoardElementType.CARD, gold, points, listOf(color))

        val selfBoard = table!!.getBoard(0)
        val initialGold = selfBoard.gold
        bonus.applyTo(player0!!)
        assertEquals((initialGold + nbCards * gold).toLong(), selfBoard.gold.toLong())
    }

    @Theory
    fun apply_countsDefeatTokens(boardPosition: RelativeBoardPosition, nbDefeatTokens: Int, points: Int, gold: Int) {
        val board = table!!.getBoard(0, boardPosition)
        for (i in 0 until nbDefeatTokens) {
            board.military.defeat()
        }

        val bonus = BonusPerBoardElement(listOf(boardPosition), BoardElementType.DEFEAT_TOKEN, gold, points, listOf())

        val selfBoard = table!!.getBoard(0)
        val initialGold = selfBoard.gold
        bonus.applyTo(player0!!)
        assertEquals((initialGold + nbDefeatTokens * gold).toLong(), selfBoard.gold.toLong())
    }

    @Theory
    fun apply_countsWonderStages(boardPosition: RelativeBoardPosition, nbStages: Int, points: Int, gold: Int) {
        val board = table!!.getBoard(0, boardPosition)
        for (i in 0 until nbStages) {
            board.wonder.placeCard(CardBack(""))
        }

        val bonus =
            BonusPerBoardElement(listOf(boardPosition), BoardElementType.BUILT_WONDER_STAGES, gold, points, listOf())

        val selfBoard = table!!.getBoard(0)
        val initialGold = selfBoard.gold
        bonus.applyTo(player0!!)
        assertEquals((initialGold + nbStages * gold).toLong(), selfBoard.gold.toLong())
    }

    companion object {

        @JvmStatic
        @DataPoints
        fun values(): IntArray = intArrayOf(0, 1, 2, 3)

        @JvmStatic
        @DataPoints
        fun colors(): Array<Color> = Color.values()

        @JvmStatic
        @DataPoints
        fun positions(): Array<RelativeBoardPosition> = RelativeBoardPosition.values()
    }
}