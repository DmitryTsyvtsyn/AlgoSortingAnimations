package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

import kotlin.math.ceil
import kotlin.math.floor

class AnimatedFloatArray(private val array: FloatArray): AnimatedArray<Float> {

    private var pushPointer = 0

    override val size: Int
        get() = pushPointer

    init {
        if (array.isEmpty()) throw IllegalStateException("The array constructor param is empty!")
    }

    override fun forcePush(value: Float) {
        array[0] = value
        pushPointer = 1
    }

    override fun push(value: Float) {
        if (pushPointer >= array.size) throw IllegalStateException("The array is out of bounds, array -> $array, index -> $pushPointer")

        array[pushPointer] = value

        pushPointer += 1
    }

    override fun reset() {
        if (pushPointer > 1) {
            pushPointer = 1
        }
    }

    override fun peek(): Float {
        if (pushPointer == 0) throw IllegalStateException("The array is empty!")

        return array[pushPointer - 1]
    }

    override fun pop(fraction: Float): Float {
        if (pushPointer == 0) throw IllegalStateException("The array is empty!")

        if (pushPointer == 1) {
            return array[0]
        }

        if (fraction == 1f) {
            array[0] = array[pushPointer - 1]
            pushPointer = 1

            return array[0]
        }

        val progress = fraction / (1f / (pushPointer - 1))

        val startValue = array[floor(progress).toInt()]
        val endValue = array[ceil(progress).toInt()]
        
        if (endValue == startValue) return endValue
        
        val progressFraction = progress - floor(progress)
        
        return linearInterpolation(startValue, endValue, progressFraction)
    }

    private fun linearInterpolation(start: Float, end: Float, fraction: Float): Float {
        return start + fraction * (end - start)
    }

}