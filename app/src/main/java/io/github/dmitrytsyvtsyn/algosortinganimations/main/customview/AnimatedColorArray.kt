package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

import androidx.core.graphics.ColorUtils
import kotlin.math.ceil
import kotlin.math.floor

class AnimatedColorArray(private val array: IntArray): AnimatedArray<Int> {

    private var pushPointer = 0

    override val size: Int
        get() = pushPointer

    override fun forcePush(value: Int) {
        if (array.isEmpty()) error("The array is out of bounds, array size is 0")

        array[0] = value
        pushPointer = 1
    }

    override fun push(value: Int) {
        if (pushPointer >= array.size) error("The array is out of bounds, array -> $array, index -> $pushPointer")

        array[pushPointer] = value

        pushPointer += 1
    }

    override fun reset() {
        if (pushPointer > 1) {
            pushPointer = 1
        }
    }

    override fun peek(): Int {
        if (pushPointer == 0) error("The array is empty!")

        return array[pushPointer - 1]
    }

    override fun pop(fraction: Float): Int {
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

        val startColor = array[floor(progress).toInt()]
        val endColor = array[ceil(progress).toInt()]

        if (endColor == startColor) return endColor

        val ratio = progress - floor(progress)
        
        return ColorUtils.blendARGB(startColor, endColor, ratio)
    }

}