package io.github.dmitrytsyvtsyn.algosortinganimations.main.customview

class SortingItemState {

    private var text: String = ""

    private val animatedProperties: Array<AnimatedArray<*>> = arrayOf(
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)),
        AnimatedFloatArray(floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)),
        AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)),
        AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)),
        AnimatedColorArray(intArrayOf(0, 0, 0, 0, 0, 0, 0, 0))
    )

    private val animationDurations = LongArray(animatedProperties.size) { 0L }

    val title: String
        get() = text

    val isAnimationRunning: Boolean
        get() = animatedProperties.any { it.size > 1 }

    fun changeTitle(title: String): SortingItemState {
        text = title
        return this
    }

    fun <T> changeDuration(key: AnimationKey<T>, duration: Long): SortingItemState {
        animationDurations[key.key] = duration
        return this
    }

    fun <T> forceValue(key: AnimationKey<T>, value: T): SortingItemState {
        (animatedProperties[key.key] as AnimatedArray<T>).forcePush(value)
        return this
    }

    fun <T> addValue(key: AnimationKey<T>, value: T): SortingItemState {
        (animatedProperties[key.key] as AnimatedArray<T>).push(value)
        return this
    }

    fun <T> addLastValue(key: AnimationKey<T>): SortingItemState {
        val animatedArray = animatedProperties[key.key] as AnimatedArray<T>
        val lastValue = animatedArray.peek()
        animatedArray.push(lastValue)
        return this
    }

    fun <T> value(key: AnimationKey<T>): T {
        return (animatedProperties[key.key] as AnimatedArray<T>).peek()
    }

    fun <T> animatedValue(key: AnimationKey<T>, animationTime: Long): T {
        var fraction = 1f

        val duration = animationDurations[key.key]
        if (duration > 0) {
            fraction = (animationTime / duration.toFloat()).coerceAtMost(1f)
        }

        return (animatedProperties[key.key] as AnimatedArray<T>).pop(fraction)
    }

    fun finishAnimation(): Boolean {
        if (isAnimationRunning) {
            animatedProperties.forEach { animatedArray -> animatedArray.pop(1f) }
            return true
        }
        return false
    }

    sealed class AnimationKey<T>(val key: Int) {
        data object StartPosition : AnimationKey<Float>(0)
        data object TopPosition : AnimationKey<Float>(1)
        data object StrokeWidth : AnimationKey<Float>(2)
        data object SelectedSize : AnimationKey<Float>(3)
        data object TextColor : AnimationKey<Int>(4)
        data object StrokeColor : AnimationKey<Int>(5)
        data object BackgroundColor : AnimationKey<Int>(6)
    }

}