package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

import kotlin.math.ceil
import kotlin.math.floor

class AnimatedFloatArray(private val array: FloatArray): AnimatedArray<Float> {

    private val tag = "AnimatedFloatArray"

    private var pushPointer = 0

    override val size: Int
        get() = pushPointer

    override fun forcePush(value: Float) {
        if (array.isEmpty()) error("The array is out of bounds, array size is 0")

        array[0] = value
        pushPointer = 1
    }

    override fun push(value: Float) {
        if (pushPointer >= array.size) error("The array is out of bounds, array -> $array, index -> $pushPointer")

        array[pushPointer] = value

        pushPointer += 1
    }

    override fun reset() {
        if (pushPointer > 1) {
            pushPointer = 1
        }
    }

    override fun peek(): Float {
        if (pushPointer == 0) error("The array is empty!")

        return array[pushPointer - 1]
    }

    override fun pop(fraction: Float): Float {
        if (pushPointer == 0) error("The array is empty!")

        if (pushPointer == 1) {
            return array[0]
        }

        if (fraction == 1f) {
            array[0] = array[pushPointer - 1]
            pushPointer = 1

            return array[0]
        }

        val progress = fraction / (1f / (pushPointer - 1))

        val previousValue = array[floor(progress).toInt()]
        val nextValue = array[ceil(progress).toInt()]
        
        val progressFraction = progress - floor(progress)

        return linearInterpolation(previousValue, nextValue, progressFraction)
    }

    private fun linearInterpolation(start: Float, end: Float, fraction: Float): Float {
        return start + fraction * (end - start)
    }

}