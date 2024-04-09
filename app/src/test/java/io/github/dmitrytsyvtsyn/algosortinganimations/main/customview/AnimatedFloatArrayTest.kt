package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import java.lang.IllegalStateException

class AnimatedFloatArrayTest {

    @Test
    fun `test init`() {
        val animatedArray1 = AnimatedFloatArray(floatArrayOf(0f))
        assertEquals(0, animatedArray1.size)

        assertThrows(IllegalStateException::class.java) {
            AnimatedFloatArray(floatArrayOf())
        }
    }

    @Test
    fun `test push`() {
        val animatedArray = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))

        animatedArray.push(10f)
        assertEquals(1, animatedArray.size)

        animatedArray.push(20f)
        assertEquals(2, animatedArray.size)

        animatedArray.push(30f)
        assertEquals(3, animatedArray.size)

        animatedArray.push(40f)
        assertEquals(4, animatedArray.size)

        animatedArray.push(50f)
        assertEquals(5, animatedArray.size)

        assertThrows(IllegalStateException::class.java) {
            animatedArray.push(60f)
        }
    }

    @Test
    fun `test forcePush`() {
        val animatedArray = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))

        animatedArray.forcePush(10f)
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(20f)
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(30f)
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(40f)
        assertEquals(1, animatedArray.size)

        animatedArray.forcePush(50f)
        assertEquals(1, animatedArray.size)
    }

    @Test
    fun `test peek`() {
        val animatedArray1 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray1.pushAll(10f, 20f, 30f, 40f, 50f)

        assertEquals(50f, animatedArray1.peek())

        val animatedArray2 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f))
        animatedArray2.pushAll(10f, 20f, 30f, 40f)
        assertEquals(40f, animatedArray2.peek())

        val animatedArray3 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f))
        animatedArray3.pushAll(10f, 20f, 30f)
        assertEquals(30f, animatedArray3.peek())

        val animatedArray4 = AnimatedFloatArray(floatArrayOf(0f, 0f))
        animatedArray4.pushAll(10f, 20f)
        assertEquals(20f, animatedArray4.peek())

        val animatedArray5 = AnimatedFloatArray(floatArrayOf(0f))
        animatedArray5.push(10f)
        assertEquals(10f, animatedArray5.peek())
    }

    @Test
    fun `test pop`() {
        val animatedArray1 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        assertThrows(IllegalStateException::class.java) {
            animatedArray1.pop(0.777f)
        }

        val animatedArray2 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray2.push(30f)
        assertEquals(30f, animatedArray2.pop(0f))
        assertEquals(30f, animatedArray2.pop(0.25f))
        assertEquals(30f, animatedArray2.pop(0.5f))
        assertEquals(30f, animatedArray2.pop(0.75f))
        assertEquals(30f, animatedArray2.pop(1f))

        val animatedArray3 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray3.pushAll(10f, 20f, 30f, 40f, 50f)
        assertEquals(50f, animatedArray3.pop(1f))
        animatedArray3.pushAll(10f, 20f, 30f, 40f)
        assertEquals(40f, animatedArray3.pop(1f))
        animatedArray3.pushAll(10f, 20f, 30f)
        assertEquals(30f, animatedArray3.pop(1f))
        animatedArray3.pushAll(10f, 20f)
        assertEquals(20f, animatedArray3.pop(1f))
        animatedArray3.pushAll(10f)
        assertEquals(10f, animatedArray3.pop(1f))

        val animatedArray4 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray4.pushAll(10f, 20f, 30f, 40f, 50f)
        assertEquals(10f, animatedArray4.pop(0f))
        assertEquals(20f, animatedArray4.pop(0.25f))
        assertEquals(30f, animatedArray4.pop(0.5f))
        assertEquals(40f, animatedArray4.pop(0.75f))
        assertEquals(50f, animatedArray4.pop(1f))
    }

    @Test
    fun `test reset`() {
        val animatedArray1 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray1.pushAll(10f, 20f, 30f, 40f, 50f)
        assertEquals(5, animatedArray1.size)
        animatedArray1.reset()
        assertEquals(1, animatedArray1.size)

        val animatedArray2 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray2.pushAll(10f, 20f, 30f, 40f)
        assertEquals(4, animatedArray2.size)
        animatedArray2.reset()
        assertEquals(1, animatedArray2.size)

        val animatedArray3 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray3.pushAll(10f, 20f, 30f)
        assertEquals(3, animatedArray3.size)
        animatedArray3.reset()
        assertEquals(1, animatedArray3.size)

        val animatedArray4 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray4.pushAll(10f, 20f)
        assertEquals(2, animatedArray4.size)
        animatedArray4.reset()
        assertEquals(1, animatedArray4.size)

        val animatedArray5 = AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f))
        animatedArray5.push(10f)
        assertEquals(1, animatedArray5.size)
        animatedArray5.reset()
        assertEquals(1, animatedArray5.size)
    }

    private fun AnimatedFloatArray.pushAll(vararg values: Float) {
        values.forEach(::push)
    }

}