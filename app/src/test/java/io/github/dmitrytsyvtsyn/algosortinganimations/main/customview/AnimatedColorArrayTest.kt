package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import java.lang.IllegalStateException

class AnimatedColorArrayTest {

    @Test
    fun `test init`() {
        val animatedArray1 = AnimatedColorArray(intArrayOf(0))
        assertEquals(0, animatedArray1.size)

        assertThrows(IllegalStateException::class.java) {
            AnimatedColorArray(intArrayOf())
        }
    }

    @Test
    fun `test push`() {
        val animatedArray = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))

        animatedArray.push(0x000000) // black
        assertEquals(1, animatedArray.size)

        animatedArray.push(0xFF0000) // red
        assertEquals(2, animatedArray.size)

        animatedArray.push(0x00FF00) // green
        assertEquals(3, animatedArray.size)

        animatedArray.push(0x0000FF) // blue
        assertEquals(4, animatedArray.size)

        animatedArray.push(0xFFFFFF) // white
        assertEquals(5, animatedArray.size)

        assertThrows(IllegalStateException::class.java) {
            animatedArray.push(0xF3F3F3) // gray
        }
    }

    @Test
    fun `test forcePush`() {
        val animatedArray = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))

        animatedArray.forcePush(0x000000) // black
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(0xFF0000) // red
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(0x00FF00) // green
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(0x0000FF) // blue
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(0xFFFFFF) // white
        assertEquals(1, animatedArray.size)
    }

    @Test
    fun `test peek`() {
        val animatedArray1 = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))
        animatedArray1.pushAll(0x000000, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFFFF)

        assertEquals(0xFFFFFF, animatedArray1.peek())

        val animatedArray2 = AnimatedColorArray(intArrayOf(0, 0, 0, 0))
        animatedArray2.pushAll(0x000000, 0xFF0000, 0x00FF00, 0x0000FF)
        assertEquals(0x0000FF, animatedArray2.peek())

        val animatedArray3 = AnimatedColorArray(intArrayOf(0, 0, 0))
        animatedArray3.pushAll(0x000000, 0xFF0000, 0x00FF00)
        assertEquals(0x00FF00, animatedArray3.peek())

        val animatedArray4 = AnimatedColorArray(intArrayOf(0, 0))
        animatedArray4.pushAll(0x000000, 0xFF0000)
        assertEquals(0xFF0000, animatedArray4.peek())

        val animatedArray5 = AnimatedColorArray(intArrayOf(0))
        animatedArray5.push(0x000000)
        assertEquals(0x000000, animatedArray5.peek())
    }

    @Test
    fun `test pop`() {
        val animatedArray1 = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))
        assertThrows(IllegalStateException::class.java) {
            animatedArray1.pop(0.777f)
        }

        val animatedArray2 = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))
        animatedArray2.push(0xFF0000) // red
        assertEquals(0xFF0000, animatedArray2.pop(0f))
        assertEquals(0xFF0000, animatedArray2.pop(0.25f))
        assertEquals(0xFF0000, animatedArray2.pop(0.5f))
        assertEquals(0xFF0000, animatedArray2.pop(0.75f))
        assertEquals(0xFF0000, animatedArray2.pop(1f))

        val animatedArray3 = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))
        animatedArray3.pushAll(0x000000, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFFFF)
        assertEquals(0xFFFFFF, animatedArray3.pop(1f))
        animatedArray3.pushAll(0x000000, 0xFF0000, 0x00FF00, 0x0000FF)
        assertEquals(0x0000FF, animatedArray3.pop(1f))
        animatedArray3.pushAll(0x000000, 0xFF0000, 0x00FF00)
        assertEquals(0x00FF00, animatedArray3.pop(1f))
        animatedArray3.pushAll(0x000000, 0xFF0000)
        assertEquals(0xFF0000, animatedArray3.pop(1f))
        animatedArray3.pushAll(0x000000)
        assertEquals(0x000000, animatedArray3.pop(1f))

        val animatedArray4 = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))
        animatedArray4.pushAll(0x000000, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFFFF)
        assertEquals(0x000000, animatedArray4.pop(0f))
        assertEquals(0xFF0000, animatedArray4.pop(0.25f))
        assertEquals(0x00FF00, animatedArray4.pop(0.5f))
        assertEquals(0x0000FF, animatedArray4.pop(0.75f))
        assertEquals(0xFFFFFF, animatedArray4.pop(1f))
    }

    @Test
    fun `test reset`() {
        val animatedArray1 = AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0))
        animatedArray1.pushAll(0x000000, 0xFF0000, 0x00FF00, 0x0000FF, 0xFFFFFF)
        assertEquals(5, animatedArray1.size)
        animatedArray1.reset()
        assertEquals(1, animatedArray1.size)

        val animatedArray2 = AnimatedColorArray(intArrayOf(0, 0, 0, 0))
        animatedArray2.pushAll(0x000000, 0xFF0000, 0x00FF00, 0x0000FF)
        assertEquals(4, animatedArray2.size)
        animatedArray2.reset()
        assertEquals(1, animatedArray2.size)

        val animatedArray3 = AnimatedColorArray(intArrayOf(0, 0, 0))
        animatedArray3.pushAll(0x000000, 0xFF0000, 0x00FF00)
        assertEquals(3, animatedArray3.size)
        animatedArray3.reset()
        assertEquals(1, animatedArray3.size)

        val animatedArray4 = AnimatedColorArray(intArrayOf(0, 0))
        animatedArray4.pushAll(0x000000, 0xFF0000)
        assertEquals(2, animatedArray4.size)
        animatedArray4.reset()
        assertEquals(1, animatedArray4.size)

        val animatedArray5 = AnimatedColorArray(intArrayOf(0))
        animatedArray5.push(0x000000)
        assertEquals(1, animatedArray5.size)
        animatedArray5.reset()
        assertEquals(1, animatedArray5.size)
    }

    private fun AnimatedColorArray.pushAll(vararg values: Int) {
        values.forEach(::push)
    }

}