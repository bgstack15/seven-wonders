package org.luxons.sevenwonders.game.cards

import org.junit.Assert.assertEquals
import org.junit.Test

class HandRotationDirectionTest {

    @Test
    fun testAgesDirections() {
        assertEquals(HandRotationDirection.LEFT, HandRotationDirection.forAge(1))
        assertEquals(HandRotationDirection.RIGHT, HandRotationDirection.forAge(2))
        assertEquals(HandRotationDirection.LEFT, HandRotationDirection.forAge(3))
    }
}