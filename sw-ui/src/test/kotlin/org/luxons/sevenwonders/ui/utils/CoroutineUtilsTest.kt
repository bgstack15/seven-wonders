package org.luxons.sevenwonders.ui.utils

import kotlinx.coroutines.delay
import org.luxons.sevenwonders.ui.test.runSuspendingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CoroutineUtilsTest {

    @Test
    fun awaitFirstTest() = runSuspendingTest {
        val s = awaitFirst(
            { delay(100); "1" },
            { delay(200); "2" },
        )
        assertEquals("1", s)
        val s2 = awaitFirst(
            { delay(150); "1" },
            { delay(50); "2" },
        )
        assertEquals("2", s2)
    }
}
