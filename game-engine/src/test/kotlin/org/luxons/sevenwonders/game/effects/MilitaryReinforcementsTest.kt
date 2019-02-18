package org.luxons.sevenwonders.game.effects

import org.junit.Assert.assertEquals
import org.junit.experimental.theories.DataPoints
import org.junit.experimental.theories.Theories
import org.junit.experimental.theories.Theory
import org.junit.runner.RunWith
import org.luxons.sevenwonders.game.SimplePlayer
import org.luxons.sevenwonders.game.resources.ResourceType
import org.luxons.sevenwonders.game.test.testBoard
import org.luxons.sevenwonders.game.test.testTable

@RunWith(Theories::class)
class MilitaryReinforcementsTest {

    @Theory
    fun apply_increaseGoldWithRightAmount(initialShields: Int, additionalShields: Int, type: ResourceType) {
        val board = testBoard(type)
        board.military.addShields(initialShields)

        val reinforcements = MilitaryReinforcements(additionalShields)
        reinforcements.applyTo(board)

        assertEquals((initialShields + additionalShields).toLong(), board.military.nbShields.toLong())
    }

    @Theory
    fun computePoints_isAlwaysZero(shields: Int) {
        val reinforcements = MilitaryReinforcements(shields)
        val player = SimplePlayer(0, testTable(5))
        assertEquals(0, reinforcements.computePoints(player).toLong())
    }

    companion object {

        @JvmStatic
        @DataPoints
        fun shieldCounts(): IntArray = intArrayOf(0, 1, 2, 3, 5)

        @JvmStatic
        @DataPoints
        fun resourceTypes(): Array<ResourceType> = ResourceType.values()
    }
}