package org.luxons.sevenwonders.game.cards

import org.junit.Assert.assertEquals
import org.junit.Test
import org.luxons.sevenwonders.game.Settings
import org.luxons.sevenwonders.game.SimplePlayer
import org.luxons.sevenwonders.game.api.Table
import org.luxons.sevenwonders.game.boards.Board
import org.luxons.sevenwonders.game.effects.ProductionIncrease
import org.luxons.sevenwonders.game.resources.Production
import org.luxons.sevenwonders.game.resources.ResourceType
import org.luxons.sevenwonders.game.resources.noTransactions
import org.luxons.sevenwonders.game.test.testCard
import org.luxons.sevenwonders.game.wonders.Wonder

class CardTest {

    @Test
    fun playCardCostingMoney() {
        val settings = Settings(3)

        val boards = listOf(
            Board(Wonder("TestWonder", ResourceType.WOOD, emptyList(), ""), 0, settings),
            Board(Wonder("TestWonder", ResourceType.STONE, emptyList(), ""), 1, settings),
            Board(Wonder("TestWonder", ResourceType.PAPYRUS, emptyList(), ""), 2, settings)
        )
        val table = Table(boards)

        val treeFarmRequirements = Requirements(1)
        val treeFarmProduction = Production()
        treeFarmProduction.addChoice(ResourceType.WOOD, ResourceType.CLAY)
        val treeFarmEffect = ProductionIncrease(treeFarmProduction, false)

        val treeFarmCard = testCard("Tree Farm", Color.BROWN, treeFarmEffect, treeFarmRequirements)

        table.getBoard(0).gold = 3
        table.getBoard(1).gold = 3
        table.getBoard(2).gold = 3
        treeFarmCard.applyTo(SimplePlayer(0, table), noTransactions())
        assertEquals(2, table.getBoard(0).gold.toLong())
        assertEquals(3, table.getBoard(1).gold.toLong())
        assertEquals(3, table.getBoard(2).gold.toLong())
    }
}